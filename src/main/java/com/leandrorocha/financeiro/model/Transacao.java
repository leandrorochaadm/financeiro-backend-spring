package com.leandrorocha.financeiro.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "transacao")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@ToString
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @NotNull
	@ManyToOne
	private Conta conta;
    
	@NotNull
	@Column(precision = 10, scale = 3)
	@JsonDeserialize(using = BigDecimalDeserializer.class)
	private BigDecimal valor;
	
	@Column(precision = 10, scale = 3)
	@JsonDeserialize(using = BigDecimalDeserializer.class)
	private BigDecimal saldo;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoTransacao tipo;

	@JsonIgnore
	@ManyToOne
	private Lancamento lancamento;

	void setValor(BigDecimal value) {
		valor = value.abs();
	}

	public BigDecimal getValor() {
		int fator = 1;
		if (conta.getTipo() == TipoConta.ATIVO)
			fator *= -1;
		if (conta.getTipo() == TipoConta.DESPESA)
			fator *= -1;
		if (tipo == TipoTransacao.D)
			fator *= -1;

		return new BigDecimal(fator).multiply(valor);
	}
    

}
