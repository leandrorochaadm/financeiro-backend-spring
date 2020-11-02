package com.leandrorocha.financeiro.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "conta")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@ToString
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    private Long id;
    
    @Getter @Setter
    @NotNull
    @Size(min = 3, max=16)
    private String nome;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "conta")
	private List<Transacao> transacao;
    
   /* @Column(name = "conta_pai")
    private Conta contaPai;
	*/

    
    
}
