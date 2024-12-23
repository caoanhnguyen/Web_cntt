package com.kma.api;

import com.kma.models.errorResponseDTO;
import com.kma.services.voteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class VoteAPI {
    @Autowired
    voteService voteServ;

    @GetMapping("/votes/check")
    public ResponseEntity<Object> checkVote(@RequestParam(required = false) Integer discussionId,
                                            @RequestParam(required = false) Integer answerId){
        try {
            boolean isVoted = voteServ.isVoted(discussionId, answerId);
            return new ResponseEntity<>(isVoted, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Discussion or answer not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
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

    @PostMapping("/discussions/{discussionId}/votes")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> voteDiscussion(@PathVariable Integer discussionId,
                                                 @RequestParam(value = "voteType") String voteType) {
        try {
            voteServ.voteDiscussion(discussionId, voteType);
            return ResponseEntity.ok("Vote successfully recorded!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Discussion not exist!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("An error occurred!");
            errorDTO.setDetails(details);
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/answers/{answerId}/votes")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> voteAnswer(@PathVariable Integer answerId,
                                             @RequestParam(value = "voteType") String voteType) {
        try {
            voteServ.voteAnswer(answerId, voteType);
            return ResponseEntity.ok("Vote successfully recorded!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Answer not exist!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("An error occurred!");
            errorDTO.setDetails(details);
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
