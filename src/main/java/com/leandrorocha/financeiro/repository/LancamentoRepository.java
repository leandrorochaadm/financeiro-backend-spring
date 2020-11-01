package com.leandrorocha.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leandrorocha.financeiro.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
