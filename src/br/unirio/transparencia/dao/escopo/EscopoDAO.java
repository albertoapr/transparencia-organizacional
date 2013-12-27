package br.unirio.transparencia.dao.escopo;

import java.util.List;

import br.unirio.transparencia.model.Escopo;
import br.unirio.transparencia.model.Organizacao;

import com.googlecode.objectify.Key;

public interface EscopoDAO {
	/**
	 * Faz a inserção ou atualização da escopo na base de dados.
	 * @param escopo
	 * @return o id objeto persistido.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Long save(Escopo escopo);
	
	/**
	 * @return Lista com todas as escopos de uma determinada organização cadastradas no banco de dados.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<Escopo> getAll(Organizacao organizacao);
	
	/**
	 * Exclui o registro da escopo na base de dados 
	 * @param escopo
	 * @return true
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Boolean remove(Escopo escopo);
	
	/**
	 * @param id filtro da pesquisa.
	 * @return Escopo com filtro no id, caso nao exista retorna <code>null</code>.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Escopo findById(Long id);
	
	Escopo findByKey(Key<Escopo> k);
}
