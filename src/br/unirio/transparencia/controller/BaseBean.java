package br.unirio.transparencia.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class BaseBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final String arquivoLeiame ="SOBRE.md";

	protected void addErrorMessage(String componentId, String errorMessage){
		addMessage(componentId, errorMessage, FacesMessage.SEVERITY_ERROR);
	}

	protected void addErrorMessage(String errorMessage){
		addErrorMessage(null, errorMessage);
	}

	protected void addInfoMessage(String componentId, String infoMessage){
		addMessage(componentId, infoMessage, FacesMessage.SEVERITY_INFO);
	}

	protected void addInfoMessage(String infoMessage){
		addInfoMessage(null, infoMessage);
	}

	
	private void addMessage(String componentId, String errorMessage, Severity severity){
		FacesMessage message =  new FacesMessage(errorMessage);
		message.setSeverity(severity);
		FacesContext.getCurrentInstance().addMessage(componentId, message);
		
	}
	
	public ArrayList<String> listaReadme(){
		ArrayList<String> texto =new ArrayList<String>();
		
		
		
		try {
		FileReader arq = new FileReader(arquivoLeiame); 
		BufferedReader lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine(); 
		while (linha != null) {
			texto.add(linha); 
			linha = lerArq.readLine();
			} 
		arq.close(); 
		return texto;
		}
		catch (IOException e) 
		{ 
			System.err.printf("Erro na abertura do arquivo: %s.", e.getMessage());
			}
		
		return null;
	}
}