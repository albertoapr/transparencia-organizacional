package br.unirio.transparencia.dao.avaliacao;

import java.util.List;

import com.googlecode.objectify.Key;

import br.unirio.transparencia.model.Avaliacao;
import br.unirio.transparencia.model.Escopo;
import br.unirio.transparencia.util.TotalizadorAvaliacoes;
import br.unirio.transparencia.util.TotalizadorAvaliacaoPorNivel;

public interface AvaliacaoDAO {
	/**
	 * Faz a inserção ou atualização da avaliacao na base de dados.
	 * @param avaliacao
	 * @return o id objeto persistido.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Long save(Avaliacao avaliacao);
	
	/**
	 * @return Lista com todas as avaliacaos cadastradas no banco de dados.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<Avaliacao> getAll();
	
	/**
	 * @return Lista com todas as avaliacaos cadastradas no banco de dados.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<TotalizadorAvaliacaoPorNivel> getTotalPorNivel();
	
	
	/**
	 * Exclui o registro da avaliacao na base de dados 
	 * @param avaliacao
	 * @return true
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Boolean remove(Avaliacao avaliacao);
	
	/**
	 * @param id filtro da pesquisa.
	 * @return Avaliacao com filtro no id, caso nao exista retorna <code>null</code>.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Avaliacao findById(Long id);

	

	List<TotalizadorAvaliacoes> getTotalizacao(boolean somenteValidas);

	List<Avaliacao> getAllByEscopo(Escopo escopo);

	Avaliacao findByKey(Key<Avaliacao> k);

}
