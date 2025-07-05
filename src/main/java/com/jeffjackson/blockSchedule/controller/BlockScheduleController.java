package com.jeffjackson.blockSchedule.controller;

import com.jeffjackson.blockSchedule.model.BlockSchedule;
import com.jeffjackson.blockSchedule.service.BlockScheduleService;
import com.jeffjackson.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class BlockScheduleController {
    @Autowired
    private BlockScheduleService blockScheduleService;

    @PostMapping("/api/blockSchedule")
    public ResponseEntity<Object> submitBlockSchedule(@RequestBody BlockSchedule blockSchedule){
        if (blockSchedule == null || blockSchedule.getDate() == null || blockSchedule.getTime() == null || blockSchedule.getType() == null) {
            MessageModel errorMessageModel = new MessageModel("Failed", "Invalid block schedule data");
            return ResponseEntity.status(HttpStatus.OK).body(errorMessageModel);
        }
        String uniqueId = blockSchedule.getDate().replace("-", "") + blockSchedule.getTime().replace(":", "").replace(" ", "");
        blockSchedule.setId(uniqueId);
        try {
            blockScheduleService.save(blockSchedule);
        }catch (Exception e){
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Failed", "Error saving block schedule");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
        MessageModel successMessageModel = new MessageModel("Success", "Schedule Saved Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(successMessageModel);
    }

    @DeleteMapping("/api/blockSchedule/{uniqueId}")
    public ResponseEntity<Object> deleteBlockSchedule(@PathVariable String uniqueId) {
        try {
            // First check if the record exists
            boolean exists = blockScheduleService.existsById(uniqueId);

            if (!exists) {
                MessageModel errorMessage = new MessageModel("Failed", "Schedule not found with ID: " + uniqueId);
                return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
            }

            blockScheduleService.deleteById(uniqueId);
            MessageModel successMessage = new MessageModel("Success", "Schedule deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(successMessage);

        } catch (Exception e) {
            e.printStackTrace();
            MessageModel errorMessage = new MessageModel("Failed", "Error deleting schedule: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
        }
    }
}
