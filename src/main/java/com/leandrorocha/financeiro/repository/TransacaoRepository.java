package com.leandrorocha.financeiro.repository;

import java.util.List;

import com.leandrorocha.financeiro.model.Transacao;

public interface TransacaoRepository {

	List<Transacao> findAll();

	List<Transacao> findByConta(Long idConta);

	Transacao findById(Long id);

	Transacao insert(Transacao transacao);

	Transacao update(Transacao transacao);

	void deleteById(Long id);

}
