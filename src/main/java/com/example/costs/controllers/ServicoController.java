package com.example.costs.controllers;

import com.example.costs.models.Servico;
import com.example.costs.services.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class ServicoController {

    @Autowired
    private ServicoService service;

    @DeleteMapping("/servico/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){

        boolean success = service.deletarById(id);

        if(success)return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/servico/atualizar/{id}")
    public ResponseEntity<String> atualizar(@RequestBody Servico servico,
                                            @PathVariable("id") Long id){

        boolean success = service.atualizarServico(id, servico);

        if(success)return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/servico/find/{id}")
    public ResponseEntity<Servico> find(@PathVariable("id") Long id){

        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
