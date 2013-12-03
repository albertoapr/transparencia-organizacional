package br.unirio.transparencia.gae.mail;

import br.unirio.transparencia.config.Configuracao;
import br.unirio.simplemvc.actions.smtp.IMailSender;
import br.unirio.simplemvc.actions.smtp.MailMessage;

import br.unirio.simplemvc.utils.StringUtils;

/**
 * Classe respons�vel pelo envio de e-mails
 * 
 * @author Marcio Barros
 */
public class GerenciadorEmail
{
	/**
	 * Inst�ncia �nica do gerenciador de e-mails
	 */
	private static GerenciadorEmail instance; 
	
	/**
	 * Mecanismo selecionado para envio de e-mails
	 */
	private IMailSender sender;

	/**
	 * Inicializa o servi�o de envio de e-mails
	 */
	public GerenciadorEmail()
	{
		this.sender = null;
	}

	/**
	 * Retorna a inst�ncia �nica do gerenciador de e-mails
	 */
	public static GerenciadorEmail getInstance()
	{
		if (instance == null)
			instance = new GerenciadorEmail();
		
		return instance;
	}
	
	/**
	 * Muda o gateway de envio de e-mails - usado para testes
	 */
	public void setMailSender(IMailSender sender)
	{
		this.sender = sender;
	}

	/**
	 * Envia um e-mail referente ao sistema de inscri��o
	 */
	public boolean envia(String nome, String email, String mensagem, String corpo)
	{
		if (this.sender == null)
			this.sender = new GAEMailSender();

		MailMessage mail = new MailMessage();
		mail.setFrom(Configuracao.getEmailOrigem());
		mail.setSubject(Configuracao.getPrefixoNotificacaoEmail() + mensagem);
		mail.addRecipient(email);	
		
		String html = "<body>";
		html += "<p>Ol&aacute; <b>" + StringUtils.escapeAccents(nome) + "!</b></p>";
		html += "<p style='text-align: center; background-color: green; border: 1px solid black; color: white; font-size: 14px; font-weight: bold; padding: 10px 10px 10px 10px;'>" + StringUtils.escapeAccents(mensagem) + "</p>";
		html += StringUtils.escapeAccents(corpo);
		html += "<p>Atenciosamente,</p>";
		html += "<p>Equipe PPGI</p>";

		html += "<p style='color:gray;font-size:9px;'>_____";
		html += "<p style='color:gray;font-size:9px;'>Este e-mail � gerado automaticamente pelo sistema de inscri��es do PPGI. Por favor, n�o responda a este e-mail.</p>";
		html += "<p style='color:gray;font-size:9px;'>Esta mensagem (incluindo todos os documentos anexos) � para uso exclusivo de seu destinat�rio e pode ";
		html += "conter informa��es privilegiadas e confidenciais. Se voc� n�o � o destinat�rio n�o deve distribuir, copiar ou arquivar a mensagem (ou seus ";
		html += "documentos anexos). Neste caso, por favor, notifique o remetente da mesma e destrua imediatamente a mensagem e seus documentos anexos.</p>";
		html += "</body>";
		
		mail.addBodyPart(html);
		return sender.send(mail);
	}
}