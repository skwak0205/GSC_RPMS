/*
* @quickreview NKR8 ZUD 22:03:29 FUN119092 - Rich text enhancement : clean-up inline formats, add subscript and superscript plugin 
*/
CKEDITOR.editorConfig = function (config) {

    config.toolbar_Basic = [
        ['Bold', 'Italic']
    ];

    /*config.toolbar_Rich = [
        ['Bold', 'Italic', 'Underline', '-', 'TextColor', 'BGColor', '-', 'PasteFromWord', '-', 'Table', '-', 'Image', '-', 'RCO']
    ];*/

    // to move the floating toolbar above the rich content title
    config.floatSpaceDockedOffsetY = 25;

    // To remove the bottom bar, we don't want to show the HTML tags
    //config.removePlugins = 'elementspath';
    config.removePlugins = 'image,forms,elementspath';

    config.allowedContent = true;

    // Paste from Word
    //FUN119092 - Automatic HTML cleanup for Paste From Word    
    //HTML cleanup is a process to create semantic HTML content after pasting content from word
    //User will not be asked for the cleanup confirmation popup
    //config.pasteFromWordPromptCleanup = true;
    config.pasteFromWordRemoveFontStyles = false;
    config.pasteFromWordRemoveStyles = false;
    //FUN119092 -  To only allow basic paragraph formatting in CKEditor Format dropdown
    //p: Paragraph (Normal)
    //h1-h6: Heading1 - Heading6
    //Disallow other formattings such as Address, Formatted and Normal(DIV)    
    config.format_tags = 'p;h1;h2;h3;h4;h5;h6';
    config.extraAllowedContent = '*{*}';
config.specialChars = config.specialChars.concat( [ [ '&alpha;', 'alpha' ],
      [ '&beta;', 'beta' ],
      [ '&gamma;', 'gamma' ],
      [ '&delta;', 'delta' ],
      [ '&epsilon;', 'epsilon' ],
      [ '&zeta;', 'zeta' ],
      [ '&eta;', 'eta' ],
      [ '&theta;', 'theta' ],
      [ '&iota;', 'iota' ],
      [ '&kappa;', 'kappa' ],
      [ '&lambda;', 'lambda' ],
      [ '&mu;', 'mu' ],
      [ '&nu;', 'nu' ],
      [ '&xi;', 'xi' ],
      [ '&omicron;', 'omicron' ],
      [ '&pi;', 'pi' ],
      [ '&rho;', 'rho' ],
      [ '&sigma;', 'sigma' ],
      [ '&tau;', 'tau' ],
      [ '&upsilon;', 'upsilon' ],
      [ '&phi;', 'phi' ],
      [ '&chi;', 'chi' ],
      [ '&psi;', 'psi' ],
      [ '&omega;', 'omega' ] ] );
    // Make dialogs simpler.
    config.removeDialogTabs = 'image:advanced;link:advanced';
    //FUN103775 -  CKEditor enhancements (Equation)
	config.mathJaxLib = widget.getUrl().substring(0,widget.getUrl().indexOf("webapps")+8)+'VENREQMathJax/2.7.4/MathJax.js?config=TeX-AMS_HTML';
	config.mathJaxClass = 'my-math';

    config.toolbar_Rich = [{
        name: 'basicstyles',
        //FUN119092 - To support basic formatting styles Underline, Subscript, Superscript
        //and new features - Copy Formatting and Remove Formatting
        //Copy formatting feature allows user to copy source text formatting to the target text fragment
        //Remove formatting feature allows user to remove already applied formatting        
        items: ['Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', 
                '-', 'CopyFormatting', 'RemoveFormat', 
                '-', 'Link', 'Unlink'
            ]
    }, {
        name: 'styles',
        items: [/*'Styles',*/ 'Format' ] //FUN119092 - Remove Styles dropdown to avoid confusing and unnecessary styles
    }, {
        name: 'colors',
        items: ['TextColor', 'BGColor']
    },
    {
        name: 'clipboard',
        items: ['SelectAll', '-', 'Cut', 'Copy', '-', 'Undo', 'Redo'/*,'Find','Replace'*/] // FUN112464 - PasteFromWord and PasteText toolbar items removed as these are not necessary
    },
	'/',
    {
        name: 'paragraph',
        items: ['NumberedList', 'BulletedList', '-', 'Indent', 'Outdent', '-', 'JustifyLeft','JustifyCenter','JustifyRight']
    },
    {
        name: 'insert',
        items: ['Table', '-', 'SpecialChar', '-', 'Mathjax', '-', 'base64image', '-', 'rcowidgetCmd']  //FUN103775 -  CKEditor enhancements (Equation)
    }];

    config.toolbar_Container = [
        ['Cut', 'Copy', 'Paste', '-', 'Undo', 'Redo']
    ];

	//VMA10 ZUD :IR-683812-3DEXPERIENCER2020x
	config.linkShowTargetTab = false

};

//IR-497282-3DEXPERIENCER2018x
CKEDITOR.on('dialogDefinition', function (ev) {
    var dialogName = ev.data.name;
    var dialogDefinition = ev.data.definition;
    var dialog = dialogDefinition.dialog;
    var editor = ev.editor;

    console.log("dialog name = " + dialogName);
    if (dialogName == 'base64imageDialog') {
    	var oldOnOK = dialogDefinition.onOk;
        dialogDefinition.onOk = function (e) {
        	oldOnOK();
        	var selectedImg = editor.getSelection();
        	if(selectedImg) selectedImg = selectedImg.getSelectedElement();
        	if(!selectedImg || selectedImg.getName() !== "img") selectedImg = null;
        	if(selectedImg) {
        		selectedImg.setAttribute("data-cke-saved-src", selectedImg.getAttribute("src"));
        	}
        };
    }else if(dialogName == 'link') //VMA10 ZUD :IR-683812-3DEXPERIENCER2020x
    {
			var target = dialogDefinition.getContents('target').get('linkTargetType');
			target['default'] = '_blank';
	}
});

