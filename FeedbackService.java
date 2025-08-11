package com.example.feedback.service;

import com.example.feedback.model.Feedback;
import com.example.feedback.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository repo;

    @Autowired
    public FeedbackService(FeedbackRepository repo) {
        this.repo = repo;
    }

    public Feedback createFeedback(Feedback feedback) {
        feedback.setCreatedAt(Instant.now());
        feedback.setUpdatedAt(Instant.now());
        return repo.save(feedback);
    }

    public Optional<Feedback> getById(String id) {
        return repo.findById(id);
    }

    public Page<Feedback> listAll(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findAll(pageable);
    }

    public Page<Feedback> listByUser(String userId, int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findByUserId(userId, pageable);
    }

    public Feedback updateFeedback(String id, Feedback patch) {
        return repo.findById(id).map(existing -> {
            if (patch.getTitle() != null)
                existing.setTitle(patch.getTitle());
            if (patch.getMessage() != null)
                existing.setMessage(patch.getMessage());
            if (patch.getRating() != 0)
                existing.setRating(patch.getRating());
            existing.setUpdatedAt(Instant.now());
            return repo.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Feedback not found: " + id));
    }

    public void deleteFeedback(String id) {
        repo.deleteById(id);
    }
}
