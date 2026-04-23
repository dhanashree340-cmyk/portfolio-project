package com.portfolio.controller;

import com.portfolio.model.Profile;
import com.portfolio.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
public class ProfileController {

    @Autowired
    ProfileRepository repo;

    String uploadDir = "src/main/resources/static/uploads/";

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("profile", new Profile());
        model.addAttribute("profiles", repo.findAll());
        return "index";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Profile profile,
                       @RequestParam("photoFile") MultipartFile photoFile,
                       @RequestParam("resumeFile") MultipartFile resumeFile) {

        try {
            if (!photoFile.isEmpty()) {
                String photoName = photoFile.getOriginalFilename();
                photoFile.transferTo(new File(uploadDir + photoName));
                profile.setPhoto("/uploads/" + photoName);
            }

            if (!resumeFile.isEmpty()) {
                String resumeName = resumeFile.getOriginalFilename();
                resumeFile.transferTo(new File(uploadDir + resumeName));
                profile.setResume("/uploads/" + resumeName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        repo.save(profile);
        return "redirect:/";
    }
}