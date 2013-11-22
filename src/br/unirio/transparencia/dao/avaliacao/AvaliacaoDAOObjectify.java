package br.unirio.transparencia.dao.avaliacao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.List;

import br.unirio.transparencia.model.Avaliacao;

import com.googlecode.objectify.Key;

public class AvaliacaoDAOObjectify implements Serializable, AvaliacaoDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long save(Avaliacao avaliacao) {
		ofy().save().entity(avaliacao).now();
		return avaliacao.getId();
	}
	
	@Override
	public List<Avaliacao> getAll() {
		return ofy().load().type(Avaliacao.class).list();
	}
	
	@Override
	public Boolean remove(Avaliacao avaliacao) {
		ofy().delete().entity(avaliacao).now();
		return true;
	}
	
	@Override
	public Avaliacao findById(Long id) {
		Key<Avaliacao> k = Key.create(Avaliacao.class, id);
		return ofy().load().key(k).get();
	}
	
}
