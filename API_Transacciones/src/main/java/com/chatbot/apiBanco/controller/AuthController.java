package com.chatbot.apiBanco.controller;


import com.chatbot.apiBanco.model.database.repository.AuthuserRepository;
import com.chatbot.apiBanco.model.database.tables.Authuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    @Autowired
    AuthuserRepository credRepo;

    @RequestMapping(value = "/credentials/add",  method = RequestMethod.POST)
    @ResponseBody
    public String addcredentials(@RequestBody Authuser input){

        String response = "Se ha creado usuario : " +
                input.getUsername() + "\n contraseña : " +
                input.getPassword() + "\n rol : " + input.getRol();

        String encoded_password = new BCryptPasswordEncoder().encode(input.getPassword());
        input.setPassword(encoded_password);
        credRepo.save(input);
        return response;
    }

    @RequestMapping(value = "/credentials/update",  method = RequestMethod.POST)
    @ResponseBody
    public String updatecredentials(@RequestBody Authuser input){

        String response = "Se ha actualizado usuario : " +
                input.getUsername() + "\n contraseña : " +
                input.getPassword() + "\n rol : " + input.getRol();

        String encoded_password = new BCryptPasswordEncoder().encode(input.getPassword());
        Authuser user = credRepo.findByusername(input.getUsername());
        user.setPassword(encoded_password);
        user.setRol(input.getRol());
        credRepo.save(user);
        return response;
    }

}
