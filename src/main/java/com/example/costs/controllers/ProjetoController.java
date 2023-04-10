package com.example.costs.controllers;

import com.example.costs.models.Projeto;
import com.example.costs.models.Servico;
import com.example.costs.services.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class ProjetoController {

    @Autowired
    private ProjetoService service;

    @PostMapping("/projeto/novo")
    public ResponseEntity<String> save(@RequestBody Projeto projeto){
        service.save(projeto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/projeto/todos")
    public ResponseEntity<List<Projeto>> list() throws InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @GetMapping("/projeto/find/{id}")
    public ResponseEntity<Projeto> find(@PathVariable("id") Long id){

        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/projeto/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){

        boolean success = service.deleteById(id);

        if(success){
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/projeto/atualizar/{id}")
    public ResponseEntity<Projeto> atualizar(@RequestBody Projeto projeto,
                                             @PathVariable("id") Long id){

        Projeto projetoNovo = service.atualizarProjeto(projeto, id);

        if(projetoNovo == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(projetoNovo);
    }


    @PostMapping("/projeto/{id}/servico/novo")
    public ResponseEntity<String> adicionarServico(@RequestBody Servico servico,
                                                   @PathVariable("id") Long id){

        boolean success = service.adicionarServico(servico, id);

        if(success){
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
