package com.example.costs.services;

import com.example.costs.models.Projeto;
import com.example.costs.models.Servico;
import com.example.costs.repositories.ProjetoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetoService{

    @Autowired
    private ProjetoRepository repository;

    @Transactional
    public void save(Projeto projeto){
        repository.save(projeto);
    }

    @Transactional
    public boolean deleteById(Long id){
        boolean exist = repository.existsById(id);

        if(exist){
            repository.deleteById(id);
            return true;
        }

        return false;
    }

    @Transactional
    public Projeto atualizarProjeto(Projeto projetoNovo, Long id){

        Projeto projetoAtual = repository.findById(id).get();

        boolean valid = Integer.parseInt(projetoNovo.getTetoDeGastos()) >= calcularCustoTotalServicos(projetoAtual);

        if(valid){
            projetoNovo.setId(projetoAtual.getId());
            projetoNovo.setServicos(projetoAtual.getServicos());
            repository.save(projetoNovo);
            return projetoNovo;
        }

        return null;
    }

    @Transactional
    public List<Projeto> findAll(){
        return repository.findAll();
    }

    @Transactional
    public Projeto findById(Long id) throws IllegalArgumentException{
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException());
    }

    @Transactional
    public boolean adicionarServico(Servico servico, Long id){

        Projeto projeto = repository.findById(id).get();

        if(cabeNoOrcamento(projeto, servico)){
            List<Servico> servicos = projeto.getServicos();
            servicos.add(servico);
            projeto.setServicos(servicos);
            repository.save(projeto);
            return true;
        }

        return false;
    }

    private int calcularCustoTotalServicos(Projeto projeto){

        List<Servico> servicos = projeto.getServicos();

        return servicos.stream()
                .map(servico -> Integer.parseInt(servico.getCusto()))
                .reduce(0, (a, b) -> a+b);
    }
    private int calcularCustoTotalServicosParaAtualizarServico(Projeto projeto, Long idServicoOld){

        List<Servico> servicos = projeto.getServicos();
        List<Servico> listaParaCalculo = servicos.stream()
                .filter(servico -> servico.getId() != idServicoOld)
                .toList();

        return listaParaCalculo.stream()
                .map(servico -> Integer.parseInt(servico.getCusto()))
                .reduce(0, (a, b) -> a+b);
    }

    public boolean cabeNoOrcamento(Projeto projeto, Servico servico){

        int disponivel = Integer.parseInt(projeto.getTetoDeGastos()) - calcularCustoTotalServicos(projeto);
        int custoServico = Integer.parseInt(servico.getCusto());

        if(custoServico <= disponivel){
            return true;
        }

        return false;
    }

    public boolean cabeNoOrcamentoAoAtualizarServico(Projeto projeto, Servico servico, Long idServicoOld){

        int disponivel = Integer.parseInt(projeto.getTetoDeGastos()) - calcularCustoTotalServicosParaAtualizarServico(projeto, idServicoOld);
        int custoServico = Integer.parseInt(servico.getCusto());

        if(custoServico <= disponivel){
            return true;
        }

        return false;
    }

    public Projeto filterProjetoByServico(Servico servico){
        List<Projeto> projetos = repository.findAll();

        List<Projeto> res = projetos.stream()
                .filter(projeto -> filter(projeto, servico.getId()))
                .toList();

        if(res.isEmpty()){
            return null;
        }

        return res.get(0);
    }

    private boolean filter(Projeto projeto, Long idServico){
        List<Long> res = projeto.getServicos().stream()
                .map(servico -> servico.getId())
                .filter(s -> s == idServico).toList();

        if(res.isEmpty()){
            return false;
        }

        return true;
    }
}
