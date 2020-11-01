package com.leandrorocha.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leandrorocha.financeiro.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {

}
