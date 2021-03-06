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
import com.leandrorocha.financeiro.model.Conta;
import com.leandrorocha.financeiro.repository.ContaRepository;


@RestController
@RequestMapping("/contas")
public class ContaController {

	@Autowired
	private ContaRepository repository;
	
	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Conta> listarTodos(){
		return repository.findAll();
	}
	
	@PostMapping
	public ResponseEntity<Conta> inserir(@Valid @RequestBody Conta conta, HttpServletResponse response) {
		Conta contaSalva =  repository.save(conta);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, contaSalva.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(contaSalva);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Conta> pesquisaPorID(@PathVariable Long id) {
		Conta conta = repository.findById(id).orElse(null);
		return conta != null ? ResponseEntity.ok().body(conta) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Conta> atualizar(@PathVariable Long id,@Valid @RequestBody Conta atualizacao){
		
		Conta conta = repository.findById(id).orElse(null);
		if(conta==null) {
			//return ResponseEntity.notFound().build();
			throw new EmptyResultDataAccessException(1); 
		}
		BeanUtils.copyProperties(atualizacao, conta, "id");
		repository.save(conta);
		
		return ResponseEntity.accepted().body(conta);
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
}
