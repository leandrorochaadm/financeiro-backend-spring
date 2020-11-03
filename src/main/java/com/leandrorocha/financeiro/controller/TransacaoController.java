package com.leandrorocha.financeiro.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.leandrorocha.financeiro.model.Transacao;
import com.leandrorocha.financeiro.repository.ContaRepository;
import com.leandrorocha.financeiro.repository.TransacaoRepository;


@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

	@Autowired
	private TransacaoRepository repository;
	
	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Transacao> listarTodos() {
		return repository.findAll();
	}
	
	@GetMapping("/saldo/{idConta}")
	public List<Transacao> listarSaldoPorConta(@PathVariable Long idConta) {
		List<Transacao> listaConta = listarPorConta(idConta);
		List<Transacao> lista = new ArrayList<Transacao>();
		BigDecimal saldo = new BigDecimal(0);

		for (Transacao transacao : listaConta) {
			saldo = saldo.add(transacao.getValor());
			transacao.setSaldo(saldo);
			lista.add(transacao);
		}
		return lista;
	}

	@PostMapping
	public ResponseEntity<Transacao> inserir(@Valid @RequestBody Transacao transacao, HttpServletResponse response) {

		Conta conta = contaRepository.findById(transacao.getConta().getId()).orElse(null);
		if (conta == null) {
			throw new EmptyResultDataAccessException(1);
		}
		Transacao transacaoSalvo = repository.insert(transacao);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, transacaoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(transacaoSalvo);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Transacao> pesquisaPorID(@PathVariable Long id) {
		Transacao transacao = repository.findById(id);
		return transacao != null ? ResponseEntity.ok().body(transacao) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/conta/{idConta}")
	public List<Transacao> listarPorConta(@PathVariable Long idConta) {
		return repository.findByConta(idConta);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Transacao> atualizar(@PathVariable Long id, @Valid @RequestBody Transacao atualizacao) {
		
		Transacao transacao = repository.findById(id);
		if (transacao == null) {
			//return ResponseEntity.notFound().build();
			throw new EmptyResultDataAccessException(1); 
		}
		BeanUtils.copyProperties(atualizacao, transacao, "id");
		repository.update(transacao);
		
		return ResponseEntity.accepted().body(transacao);
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
}
