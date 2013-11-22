/**
 * Transforms a div into a document upload field
 */
function documentField(divField) {
	fieldName = divField.attr("fieldName");
	key = divField.attr("key");
	bucket = divField.attr("bucket");
	entity = divField.attr("entity");
	id = divField.attr("itemid");
	type = divField.attr("directory");
	
	var styleAttr = divField.attr("style");
	
	if (!styleAttr)
		styleAttr = "";
	
	divField.attr("style", styleAttr + "; border: 1px solid lightgray; padding-left: 4px; padding-right: 4px; overflow: hidden; font-size:12px; background-color: white;");
	
	var keyField = $("<input type='hidden' id='" + fieldName + "' name='" + fieldName + "' value='" + key + "' bucket='" + bucket + "'></input>");
	keyField.appendTo(divField);
	
	var nameField = $("<div id='nome_" + fieldName + "' bucket='" + bucket + "' entity='" + entity + "' entityid='" + id + "' type='" + type + "'></div>");

	if (key.length > 0)
		nameField.html(createDocumentText(fieldName, entity, key));
	else
		nameField.html(createNoDocumentText(fieldName, entity));
	
	nameField.appendTo(divField);
}

/**
 * Clear the information selected on a document field
 */
function setDocumentFieldValue(fieldName, value) {
	var field = $("#" + fieldName);
	field.val(value);
	
	var divField = $("#div" + fieldName);
	var entity = divField.attr("entity");
    
    if (value.length > 0)
    	$("#nome_" + fieldName).html(createDocumentText(fieldName, entity, value));
    else
    	$("#nome_" + fieldName).html(createNoDocumentText(fieldName, entity));
}

/**
 * Creates the text and control to appear when no document is selected
 */
function createNoDocumentText(fieldName, entity) {
	var s = "<a href=\"javascript:void(0)\" onclick=\"javascript:return openUploadDialog('" + fieldName + "', '" + entity + "');\" style='color:navy;'>Selecionar</a>";
	return "Nenhum documento selecionado - " + s;
}

/**
 * Creates the text and controls to appear when a document is selected
 */
function createDocumentText(fieldName, entity, value) {
	var keys = value.split(",");
	s = "";
	
	for (var i = 0; i < keys.length; i++)
	{
		s += "<img src='/img/icons/pdf.jpg'>&nbsp;";
		s += "<a href=\"/view?key=" + keys[i] + "&type=" + entity + "\" target='_blank' style='color:navy;'>Visualizar</a>";
		s += " - <a href=\"javascript:void(0)\" onclick=\"javascript:return removeDocument('" + fieldName + "', '" + keys[i] + "');\" style='color:navy;'>Remover</a>";
		s += "<br>";
	}

	if (keys.length < 8)
		s += "<a href=\"javascript:void(0)\" onclick=\"javascript:return openUploadDialog('" + fieldName + "', '" + entity + "');\" style='color:navy;'>Adicionar</a><br>";

	return s;
}

/**
 * Creates a short set of document links
 */
function createDocumentView(entity, value) {
	if (!value)
		return "";
	
	var keys = value.split(",");
	s = "";
	
	for (var i = 0; i < keys.length; i++)
		s += "<a href=\"/view?key=" + keys[i] + "&type=" + entity + "\" target='_blank' style='color:navy;'><img src='/img/icons/pdf.jpg'></a>";

	return s;
}

/**
 * Opens the dialog box to upload a document
 */
function openUploadDialog(fieldName, entity) {
	
	/* Gets the placeholder for the selected documents in the HTML page */
	var placeholder = $("#nome_" + fieldName);
	
	/* Removes a previous form for the same document, if any */
    var previousForm = $('#uploadform_' + fieldName);
    if (previousForm) previousForm.remove();
	
	/* Collect data regarding the document to be uploaded from the placeholder */
    var bucket = placeholder.attr("bucket");
    var entity = placeholder.attr("entity");
	var id = placeholder.attr("entityid");
	var type = placeholder.attr("type");
	
	/* Creates a new dialog box and adds to the HTML page */
	var dialogBox = $(createUploadDialog(fieldName, bucket));
	dialogBox.dialog({ minWidth: 600, minHeight: 220, maxWidth: 600, maxHeight: 220, autoOpen: false, modal: true });

	$("#btEnvia_" + fieldName).click(function (event) {
		sendDocument(fieldName, entity);
		return false;
	});

	$("#btCancela_" + fieldName).click(function (event) {
		dialogBox.dialog("close");
		dialogBox.remove();
		return false;
	});

	/* Get the reference data to connect to S3 and upload a document */
	postData = "bucket=" + bucket + "&entidade=" + entity + "&id=" + id + "&tipo=" + type;
	ajaxCall("/documento/pegaProximoDocumento.do", postData, false, function (data) {
		$("#AWSAccessKeyId_" + fieldName).val(data.accessKey);
		$("#key_" + fieldName).val(data.filename);
		$("#policy_" + fieldName).val(data.policy);
		$("#signature_" + fieldName).val(data.signature);
		$("#redirect_" + fieldName).val(data.callback);
	});
	
	/* Finally, opens the dialog box */
	dialogBox.dialog("open");
	return false;
}

/**
 * Creates the dialog box to upload a document
 */
function createUploadDialog(fieldName, bucket) {
	s = '<div id="uploadform_' + fieldName + '" title="Envio de Documentos para a BV Rio" style="width:500px; font-size:12px;">';
	s += '<p>Selecione no campo abaixo o documento que dever&aacute; ser enviado para a BV Rio. Somente ';
	s += 'documentos em formato PDF e com at&eacute; 5Mb de dados poder&atilde;o ser enviados.</p>';

	s += '<form action="http://' + bucket + '.s3.amazonaws.com/" id="s3form_' + fieldName + '" enctype="multipart/form-data" method="post">';
	s += '<input type="hidden" name="AWSAccessKeyId" id="AWSAccessKeyId_' + fieldName + '" value="" />';
	s += '<input type="hidden" name="acl" id="acl_' + fieldName + '" value="private" />';
	s += '<input type="hidden" name="key" id="key_' + fieldName + '" value="" />';
	s += '<input type="hidden" name="policy" id="policy_' + fieldName + '" value="" />';
	s += '<input type="hidden" name="Content-Type" id="contentType_' + fieldName + '" value="application/pdf" />';
	s += '<input type="hidden" name="signature" id="signature_' + fieldName + '" value="" />';
	s += '<input type="hidden" name="success_action_redirect" id="redirect_' + fieldName + '" value="" />';
		
	s += '<br>';
	s += '<label for="file">Arquivo:</label>&nbsp;';
	s += '<input type="file" name="file" id="file_' + fieldName + '" style="border: 1px solid gray;" />';
	s += '<input type="button" id="btEnvia_' + fieldName + '" name="btSubmit" value="Envia"/>';
	s += '<input type="button" id="btCancela_' + fieldName + '" name="btCancel" value="Cancela"/>';
	s += '<br>';

	s += '<img id="loadingImage_' + fieldName + '" src="/img/ajax-loader.gif" style="margin-left: 55px; display: none;"/><br>';
	s += '</form>';
	s += '</div>';
	
	return s;
}

/**
 * Sends a document to S3
 */
function sendDocument(fieldName, entity)
{
	var fileToUpload = $("#file_" + fieldName).val();

    if (!fileToUpload)
    {
        alert("Selecione um arquivo para ser enviado para a BV Rio.");
        return false;
    }
    
    var extension = fileToUpload.substring(fileToUpload.lastIndexOf("."), fileToUpload.length).toLowerCase();
    
    if (extension != ".pdf") 
    {
        alert("Selecione um arquivo PDF para ser enviado para a BV Rio.");
        return false;
    }
    
    $("#loadingImage_" + fieldName).css("display", "block");
    
    var s3url = $("#s3form_" + fieldName).attr("action");
    var s3formEle = YAHOO.util.Dom.get('s3form_' + fieldName);
    YAHOO.util.Connect.setForm(s3formEle, true);
    YAHOO.util.Connect.asyncRequest('POST', s3url, {
        upload: function(o) {
            if (o.responseText)
            {
                var response = JSON.parse(o.responseText);
                var currentKey = $("#" + fieldName).val();
                
                if (currentKey.length > 0)
                	currentKey = currentKey + "," + response.key;
                else
                	currentKey = response.key;
                
                $("#" + fieldName).val(currentKey);
        		$("#nome_" + fieldName).html(createDocumentText(fieldName, entity, currentKey));
            }
            else
                alert("Ocorreu um erro durante o upload do arquivo.");                           
    		
            $('#uploadform_' + fieldName).dialog('close');
            $('#uploadform_' + fieldName).remove();
        },

        failure: function(o) {
            alert("Ocorreu um erro ao enviar o documento: " + o.statusText);
            $('#uploadform_' + fieldName).dialog('close');
            $('#uploadform_' + fieldName).remove();
        }
    });
}

/**
 * Removes a selected document from the control
 */
function removeDocument(fieldName, key)
{
	var keys = $("#" + fieldName).val();
	keys = keys.replace(key + ",", "");
	keys = keys.replace("," + key, "");
	keys = keys.replace(key, "");
	setDocumentFieldValue(fieldName, keys);
	return false;
}