package com.jeffjackson.booking.service;

import com.jeffjackson.blockSchedule.model.BlockSchedule;
import com.jeffjackson.blockSchedule.service.BlockScheduleService;
import com.jeffjackson.booking.config.RazorpayService;
import com.jeffjackson.booking.model.*;
import com.jeffjackson.booking.reporsitory.BookingRepository;
import com.jeffjackson.enquiry.model.Enquiry;
import com.jeffjackson.enquiry.model.PaginatedResponse;
import com.jeffjackson.service.EmailService;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RazorpayService razorpayService;

    private final RazorpayClient razorpayClient;
    private final BlockScheduleService blockScheduleService;
    private final EmailService emailService;
    @Value("${cc.email.list}")
    private String [] ccEmailList;
    private static final String DATE_PATTERN = "MM-dd-yyyy";
    private static final String TIME_PATTERN = "h:mm a";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN)
            .withLocale(Locale.US); // For AM/PM formatting


    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          RazorpayService razorpayService,
                          RazorpayClient razorpayClient, BlockScheduleService blockScheduleService,
                          EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.razorpayService = razorpayService;
        this.razorpayClient = razorpayClient;
        this.blockScheduleService = blockScheduleService;
        this.emailService = emailService;
    }

    public Booking createBooking(BookingRequest request) {
        // Validate date format
        try {
            LocalDate.parse(request.getEventDate(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid event date format. Required format: " + DATE_PATTERN +
                            " (e.g., 07-12-2025)");
        }

        // Validate time format
        try {
            LocalTime.parse(request.getEventTime(), TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid event time format. Required format: " + TIME_PATTERN +
                            " (e.g., 5:00 PM)");
        }

        //Generate a unique Key which we will use for validation if it exists we will not save it in DB
        String key = request.getEventDate().replace("-", "")
                + request.getEventTime().replace(":", "").replace(" ", "")
                + request.getEmail().split("@")[0];

        Booking result = bookingRepository.findByKey(key.toUpperCase());
        if (result != null) {
            throw new IllegalArgumentException("Booking already exists. Your Booking Id is: " + result.getUniqueId());
        }
        try{
           request.setKey(key.toUpperCase());
        }catch (Exception e) {
            throw new IllegalArgumentException("Invalid key format. Please check the input data.");
        }

        // Create booking
        Booking booking = new Booking();
        String uniqueId = "BK" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        booking.setUniqueId(uniqueId);
        booking.setClientName(request.getClientName());
        booking.setEmail(request.getEmail());
        booking.setPhone(request.getPhone());
        booking.setEventType(request.getEventType());
        booking.setEventDate(request.getEventDate());
        booking.setEventTime(request.getEventTime());
        booking.setStreet(request.getStreet());
        booking.setApt(request.getApt());
        booking.setCity(request.getCity());
        booking.setState(request.getState());
        booking.setMessage(request.getMessage());
        booking.setAmount(request.getAmount());
        booking.setPaymentCompleted(false);
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalAmount(request.getAmount());
        booking.setDepositReceived(request.getAmount());
        booking.setRemainingAmount("0");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        booking.setKey(key.toUpperCase());

//        booking.setKey(key);

        // Save booking
        booking = bookingRepository.save(booking);

        // Block the schedule
        blockSchedule(booking);

        return booking;
    }

    public PaymentOrder createPaymentOrder(String bookingId, double amount) throws RazorpayException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        return razorpayService.createOrder(amount, "USD", "booking_" + bookingId, bookingId);
    }

    public Booking completeBooking(String paymentId, String orderId, String signature) throws RazorpayException, MessagingException {
        PaymentOrder order = razorpayService.paymentOrderRepository.findByRazorpayOrderId(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        // Verify payment signature
        if (!razorpayService.verifyPaymentSignature(orderId, paymentId, signature)) {
            throw new IllegalArgumentException("Invalid payment signature");
        }

        // Capture payment details
        Payment payment = razorpayClient.payments.fetch(paymentId);
        Map<String, Object> paymentDetails = new HashMap<>();
        paymentDetails.put("method", payment.get("method"));
        paymentDetails.put("bank", payment.get("bank"));
        paymentDetails.put("wallet", payment.get("wallet"));
        paymentDetails.put("vpa", payment.get("vpa"));
        paymentDetails.put("email", payment.get("email"));
        paymentDetails.put("contact", payment.get("contact"));

        razorpayService.capturePayment(paymentId, orderId, signature, paymentDetails);

        // Update booking status
        Booking booking = bookingRepository.findById(order.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentCompleted(true);
        booking.setUpdatedAt(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        // Send confirmation emails
//        sendConfirmationEmails(booking);

        return booking;
    }

    private void blockSchedule(Booking booking) {
        BlockSchedule blockSchedule = new BlockSchedule();
        blockSchedule.setType("BOOKING");
        blockSchedule.setDate(booking.getEventDate());
        blockSchedule.setTime(booking.getEventTime());
        blockSchedule.setReason("Booking confirmed with ID: " + booking.getUniqueId());
        String blockKey = blockSchedule.getDate().replace("-", "") + blockSchedule.getTime().replace(":", "").replace(" ", "");
        blockSchedule.setId(blockKey);
        blockScheduleService.save(blockSchedule);
    }

    private void sendConfirmationEmails(Booking booking) throws MessagingException {
        // Send email to admin
        Map<String, Object> adminModel = new HashMap<>();
        adminModel.put("clientName", booking.getClientName());
        adminModel.put("email", booking.getEmail());
        adminModel.put("phone", booking.getPhone());
        adminModel.put("eventType", booking.getEventType());
        adminModel.put("eventDate", booking.getEventDate().format(String.valueOf(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
        adminModel.put("eventTime", booking.getEventTime());
        adminModel.put("address", booking.getStreet() + ", " + booking.getCity() + ", " + booking.getState());
        adminModel.put("message", booking.getMessage());
        adminModel.put("bookingId", booking.getUniqueId());
        adminModel.put("createdAt", booking.getCreatedAt());
        adminModel.put("amount", booking.getAmount());

        emailService.sendEmailFromTemplate(
                "djjeffjackson@gmail.com",
                "New Booking Confirmed: " + booking.getUniqueId(),
                "admin-booking-confirmation",
                adminModel
        );

        // Send email to client
        Map<String, Object> clientModel = new HashMap<>();
        clientModel.put("clientName", booking.getClientName());
        clientModel.put("eventType", booking.getEventType());
        clientModel.put("eventDate", booking.getEventDate().format(String.valueOf(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
        clientModel.put("eventTime", booking.getEventTime());
        clientModel.put("bookingId", booking.getUniqueId());
        clientModel.put("amount", booking.getAmount());

        emailService.sendEmailFromTemplate(
                booking.getEmail(),
                "Your Booking Confirmation - " + booking.getUniqueId(),
                "client-booking-confirmation",
                clientModel
        );
    }

    public PaginatedResponse<BookingResponse> getPaginatedBookings(int page, int size) {
        // Create page request
        Pageable pageable = PageRequest.of(page, size);

        // Fetch paginated data from repository
        Page<Booking> bookingPage = bookingRepository.findAllByOrderByCreatedAtDesc(pageable);

        // Convert Booking entities to BookingResponse DTOs
        List<BookingResponse> content = bookingPage.getContent()
                .stream()
                .map(this::convertToBookingResponse)
                .collect(Collectors.toList());

        // Create new page with converted content
        Page<BookingResponse> responsePage = new PageImpl<>(
                content,
                pageable,
                bookingPage.getTotalElements()
        );

        // Return paginated response
        return new PaginatedResponse<>(responsePage);
    }

    private BookingResponse convertToBookingResponse(Booking booking) {
        // Combine address fields
        String address = String.join(", ",
                booking.getStreet(),
                booking.getApt(),
                booking.getCity(),
                booking.getState()).replaceAll(", , ", ", ");

        return new BookingResponse(
                booking.getUniqueId(),
                booking.getClientName(),
                booking.getEmail(),
                booking.getPhone(),
                booking.getEventType(),
                booking.getType(),
                booking.getEventDate(),
                booking.getEventTime(),
                address,
                booking.getMessage(),
                booking.getStatus().name(),
                booking.getDepositReceived(),
                booking.getTotalAmount(),
                booking.getRemainingAmount(),
                booking.getAgreementUrl(),
                booking.getCreatedAt()
        );
    }
}