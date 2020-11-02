package com.leandrorocha.financeiro.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import com.leandrorocha.financeiro.repository.TransacaoRepository;

import io.restassured.http.ContentType;
@WebMvcTest
class TrasacaoTest {

	@Autowired
	private TransacaoController controller;

	@MockBean
	private TransacaoRepository repository;

	@BeforeEach
	public void setup() {
		standaloneSetup(this.controller);
	}

	@Test
	public void deveRetornaCreated_QuandoBuscarTrasacao() {
		Mockito.when(this.repository);
		
		given().accept(ContentType.JSON).when().get("/transacoes").then().statusCode(HttpStatus.OK.value());
	}

}
