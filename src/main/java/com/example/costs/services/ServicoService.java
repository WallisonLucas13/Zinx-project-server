package com.example.costs.services;

import com.example.costs.models.Projeto;
import com.example.costs.models.Servico;
import com.example.costs.repositories.ServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository repository;

    @Autowired
    private ProjetoService projetoService;

    @Transactional
    public boolean deletarById(Long id){
        boolean exist = repository.existsById(id);

        if(exist){
            repository.deleteById(id);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean atualizarServico(Long idServico, Servico servicoNovo){

        Servico servico = repository.findById(idServico).get();
        Projeto projeto = projetoService.filterProjetoByServico(servico);

        if(projetoService.cabeNoOrcamentoAoAtualizarServico(projeto, servicoNovo, servico.getId())){
            servicoNovo.setId(servico.getId());
            repository.save(servicoNovo);
            return true;
        }
        System.out.println("Não cabe no Orçamento!");
        return false;
    }

    @Transactional
    public Servico findById(Long id) throws IllegalArgumentException{
        return repository.findById(id).orElseThrow(IllegalArgumentException::new);
    }



}
