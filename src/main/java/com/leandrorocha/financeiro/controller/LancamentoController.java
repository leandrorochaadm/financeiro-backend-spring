package com.leandrorocha.financeiro.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.leandrorocha.financeiro.event.RecursoCriadoEvent;
import com.leandrorocha.financeiro.model.Lancamento;
import com.leandrorocha.financeiro.repository.LancamentoRepository;


@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

	@Autowired
	private LancamentoRepository repository;
	
	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Lancamento> listarTodos(){
		return repository.findAll();
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> inserir(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = repository.save(lancamento);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> pesquisaPorID(@PathVariable Long id) {
		Lancamento lancamento = repository.findById(id).orElse(null);
		return lancamento != null ? ResponseEntity.ok().body(lancamento) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long id,@Valid @RequestBody Lancamento atualizacao){
		
		Lancamento lancamento = repository.findById(id).orElse(null);
		if(lancamento==null) {
			//return ResponseEntity.notFound().build();
			throw new EmptyResultDataAccessException(1); 
		}
		BeanUtils.copyProperties(atualizacao, lancamento, "id");
		repository.save(lancamento);
		
		return ResponseEntity.accepted().body(lancamento);
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
}
