package com.leandrorocha.financeiro.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.leandrorocha.financeiro.model.Transacao;

@Repository
public class TransacaoRepositoryImp implements TransacaoRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public List<Transacao> findAll() {
		return em.createQuery("from Transacao", Transacao.class).getResultList();
	}

	@Override
	@Transactional
	public List<Transacao> findByConta(Long idConta) {
		return em.createQuery("from Transacao where conta_id = :idConta", Transacao.class)
				.setParameter("idConta", idConta)
				.getResultList();
	}

	@Override
	@Transactional
	public Transacao findById(Long id) {
		return em.find(Transacao.class, id);
	}

	@Override
	@Transactional
	public Transacao insert(Transacao transacao) {
		em.persist(transacao);
		return findById(transacao.getId());
	}

	@Override
	@Transactional
	public Transacao update(Transacao transacao) {
		return em.merge(transacao);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		Transacao trasacao = findById(id);
		em.remove(trasacao);

	}

}
