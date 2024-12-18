package com.kma.api;

import com.kma.models.*;
import com.kma.services.answerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/discussions")
public class AnswerAPI {
    @Autowired
    answerService answerServ;

    @GetMapping(value="/{discussionId}/answers")
    public ResponseEntity<Object> getAllAnswerOfDiscussion(@PathVariable Integer discussionId,
                                                           @RequestParam(required = false, defaultValue = "0") int page,
                                                           @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<answerDTO> DTO =  answerServ.getAllAnswerOfDiscussion(discussionId, page, size);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("An error occurred!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{discussionId}/answers")
    public ResponseEntity<Object> addAnswer(@PathVariable Integer discussionId,
                                            @ModelAttribute answerRequestDTO answerReqDTO) {
        try {
            answerServ.addAnswer(discussionId, answerReqDTO);
            return ResponseEntity.ok("Add successfully!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Author not exist!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @PutMapping(value = "/{discussionId}/answers/{answerId}")
    public ResponseEntity<Object> updateAnswer(@PathVariable Integer discussionId,
                                               @PathVariable Integer answerId,
                                               @ModelAttribute answerRequestDTO answerReqDTO) {

        try {
            answerServ.updateAnswer(discussionId, answerId, answerReqDTO);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Discussion or answer not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }


    @DeleteMapping(value = "/{discussionId}/answers/{answerId}")
    public ResponseEntity<Object> deleteAnswer(@PathVariable Integer discussionId,
                                               @PathVariable Integer answerId) {
        try {
            answerServ.deleteAnswer(discussionId, answerId);
            return ResponseEntity.ok("Delete successfully!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Discussion or answer not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}
