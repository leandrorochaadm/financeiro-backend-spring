package com.leandrorocha.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leandrorocha.financeiro.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {


}
