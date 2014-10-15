Tapestry.Initializer.CK_FCKEditor = function(spec) {
	
	var textArea = $(spec.CLIENT_ID);
	var fckEditorInstance = new FCKeditor(spec.CLIENT_ID);
	fckEditorInstance.BasePath = spec.BASE_PATH;
	fckEditorInstance.Height = spec.HEIGHT;
	fckEditorInstance.Width = spec.WIDTH;
	
	if(spec.CUSTOM_CONFIG_PATH) 
	{
		fckEditorInstance.Config['CustomConfigurationsPath'] = spec.CUSTOM_CONFIG_PATH;
	}
	
	if(spec.TOOLBAR_SET) 
	{
		fckEditorInstance.ToolbarSet = spec.TOOLBAR_SET;
	}
	fckEditorInstance.ReplaceTextarea();
	
	var handleSubmit = function(){	
		var fckInstance = FCKeditorAPI.GetInstance(this.id);
		var content = fckInstance.GetXHTML();
		this.value = content;
		FCKeditorAPI = null; 
		__FCKeditorNS = null;
	};
	
	textArea.form.observe(Tapestry.FORM_PREPARE_FOR_SUBMIT_EVENT, handleSubmit.bindAsEventListener(textArea));
};