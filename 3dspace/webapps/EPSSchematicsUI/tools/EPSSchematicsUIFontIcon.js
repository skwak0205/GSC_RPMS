/// <amd-module name='DS/EPSSchematicsUI/tools/EPSSchematicsUIFontIcon'/>
define("DS/EPSSchematicsUI/tools/EPSSchematicsUIFontIcon", ["require", "exports", "DS/EPSSchematicsModelWeb/EPSSchematicsModelEnums", "DS/EPSSchematicsModelWeb/EPSSchematicsBlockLibrary", "DS/EPSSchematicsUI/EPSSchematicsUIEnums", "DS/EPSSchematicsUI/tools/EPSSchematicsUIDom", "DS/EPSSchematicsUI/tools/EPSSchematicsUIWUXTools", "i18n!DS/EPSSchematicsUI/assets/nls/EPSSchematicsUINLS"], function (require, exports, ModelEnums, BlockLibrary, UIEnums, UIDom, UIWUXTools, UINLS) {
    "use strict";
    /* eslint-enable no-unused-vars */
    var UIFontIcon = /** @class */ (function () {
        function UIFontIcon() {
        }
        /**
         * Creates a 3DS font icon.
         * @public
         * @static
         * @param {string} iconName - The name of the icon.
         * @param {IDomOptions} [options] - The creation options.
         * @returns {HTMLSpanElement} The created font icon element.
         */
        UIFontIcon.create3DSFontIcon = function (iconName, options) {
            if (options === void 0) { options = {}; }
            options.className = Array.isArray(options.className) ? options.className : (typeof options.className === 'string' ? [options.className] : []);
            options.className.unshift(this.getWUX3DSClassName(iconName));
            return this.createFontIcon(UIEnums.EFontFamily.eFont3DS, options);
        };
        /**
         * Creates a Font Awesome font icon.
         * @public
         * @static
         * @param {string} iconName - The name of the icon.
         * @param {IDomOptions} [options] - The creation options.
         * @returns {HTMLSpanElement} The created font icon element.
         */
        UIFontIcon.createFAFontIcon = function (iconName, options) {
            if (options === void 0) { options = {}; }
            options.className = Array.isArray(options.className) ? options.className : (typeof options.className === 'string' ? [options.className] : []);
            options.className.unshift(this.getWUXFAClassName(iconName));
            return this.createFontIcon(UIEnums.EFontFamily.eFontAwesome, options);
        };
        /**
         * Creates a font icon from the provided icon definition.
         * @public
         * @static
         * @param {ICommandIcon} iconDefinition - The icon definition.
         * @param {IDomOptions} [options] - The creation options.
         * @returns {HTMLSpanElement} The created font icon element.
         */
        UIFontIcon.createFontIconFromDefinition = function (iconDefinition, options) {
            if (options === void 0) { options = {}; }
            options.className = Array.isArray(options.className) ? options.className : (typeof options.className === 'string' ? [options.className] : []);
            var fontFamilyValue = UIEnums.EFontFamily[iconDefinition.fontFamily];
            var fontIconElt;
            if (fontFamilyValue === UIEnums.EFontFamily.eFontAwesome) {
                fontIconElt = this.createFAFontIcon(iconDefinition.name, options);
            }
            else if (fontFamilyValue === UIEnums.EFontFamily.eFont3DS) {
                fontIconElt = this.create3DSFontIcon(iconDefinition.name, options);
            }
            return fontIconElt;
        };
        /**
         * Gets the WUX icon object from the command definition.
         * @public
         * @static
         * @param {ICommandType} command - The command definition.
         * @returns {IWUXStructIcon} The WUX icon.
         */
        UIFontIcon.getWUXIconFromCommand = function (command) {
            var wuxIcon;
            if (command !== undefined && command.icon !== undefined) {
                wuxIcon = {
                    iconName: command.icon.name,
                    fontIconFamily: UIEnums.EFontFamily[command.icon.fontFamily]
                };
            }
            return wuxIcon;
        };
        /**
         * Gets the WUX Font Awesome icon definition.
         * @public
         * @static
         * @param {string} iconName - The Font Awesome icon name.
         * @param {string} [fontIconSize=undefined] - The font icon size.
         * @returns {IWUXStructIcon} The WUX Font Awesome icon definition.
         */
        UIFontIcon.getWUXFAIconDefinition = function (iconName, fontIconSize) {
            return {
                iconName: iconName,
                fontIconFamily: UIEnums.EFontFamily.eFontAwesome,
                fontIconSize: fontIconSize
            };
        };
        /**
         * Gets the WUX 3DS icon definition.
         * @public
         * @static
         * @param {string} iconName - The 3DS icon name.
         * @returns {IWUXStructIcon} The WUX 3DS icon definition.
         */
        UIFontIcon.getWUX3DSIconDefinition = function (iconName) {
            return {
                iconName: iconName,
                fontIconFamily: UIEnums.EFontFamily.eFont3DS
            };
        };
        /**
         * Gets the WUX icon from the block category.
         * @public
         * @static
         * @param {string} fullCategoryName - The full name of the category.
         * @returns {IWUXStructIcon} The WUX icon definiton.
         */
        UIFontIcon.getWUXIconFromBlockCategory = function (fullCategoryName) {
            var icon = { iconName: '3dpart', fontIconFamily: UIEnums.EFontFamily.eFont3DS };
            var categoryDoc = BlockLibrary.getCategoryDocumentation(fullCategoryName);
            if (categoryDoc !== undefined) {
                var catIcon = categoryDoc.getIcon();
                if (catIcon !== undefined) {
                    icon.iconName = catIcon.name;
                    icon.fontIconFamily = UIEnums.EFontFamily[catIcon.fontFamily];
                }
            }
            return icon;
        };
        /**
         * Creates the font icon from the given block full category name.
         * If the font icon is created, it will be injected into the given parent element.
         * @public
         * @static
         * @param {string} fullCategoryName - The full name of the category.
         * @param {IWUXElement} [parentElement] - The parent element.
         * @param {string|Array<string>} [className] - The className or list of className.
         * @param {boolean} [displayTooltip=false] - True to display a tooltip.
         * @returns {Element} The created icon element.
         */
        UIFontIcon.createIconFromBlockCategory = function (fullCategoryName, parentElement, className, displayTooltip) {
            if (className === void 0) { className = []; }
            if (displayTooltip === void 0) { displayTooltip = false; }
            var iconClassName = [this.WUX3DSClassName, UIFontIcon.getWUX3DSClassName('5x'), UIFontIcon.getWUX3DSClassName('3dpart')];
            var categoryDoc = BlockLibrary.getCategoryDocumentation(fullCategoryName);
            if (categoryDoc !== undefined) {
                var catIcon = categoryDoc.getIcon();
                if (catIcon !== undefined && catIcon.name !== undefined && catIcon.fontFamily !== undefined) {
                    var fontFamilyValue = UIEnums.EFontFamily[catIcon.fontFamily];
                    if (fontFamilyValue === UIEnums.EFontFamily.eFontAwesome) {
                        iconClassName = [this.WUXFAClassName, UIFontIcon.getWUXFAClassName('5x'), UIFontIcon.getWUXFAClassName(catIcon.name)];
                    }
                    else if (fontFamilyValue === UIEnums.EFontFamily.eFont3DS) {
                        iconClassName = [this.WUX3DSClassName, UIFontIcon.getWUX3DSClassName('5x'), UIFontIcon.getWUX3DSClassName(catIcon.name)];
                    }
                }
            }
            if (parentElement !== undefined && fullCategoryName !== '' && displayTooltip) {
                var fullName = categoryDoc !== undefined ? categoryDoc.getFullName() : '';
                parentElement.tooltipInfos = UIWUXTools.createTooltip({
                    title: UINLS.get('tooltipTitleBlockDocumentation', { category: fullName }),
                    shortHelp: UINLS.get('tooltipShortHelpBlockDocumentation')
                });
            }
            return UIDom.createElement('span', {
                parent: parentElement,
                className: iconClassName.concat(className)
            });
        };
        /**
         * Gets the WUX 3DS class name from the provided icon name.
         * @public
         * @static
         * @param {string} iconName - The WUX 3DS icon name.
         * @returns {string} The WUX 3DS class name.
         */
        UIFontIcon.getWUX3DSClassName = function (iconName) {
            return this.WUX3DSClassName + '-' + iconName;
        };
        /**
         * Gets the WUX Font Awesome class name from the provided icon name.
         * @public
         * @static
         * @param {string} iconName - The WUX Font Awesome icon name.
         * @returns {string} The WUX Font Awesome class name.
         */
        UIFontIcon.getWUXFAClassName = function (iconName) {
            return this.WUXFAClassName + '-' + iconName;
        };
        /**
         * Creates a font icon.
         * @private
         * @static
         * @param {EFontFamily} fontFamily - The font family enumeration.
         * @param {Object} [options] - The creation options.
         * @returns {HTMLSpanElement} The created font icon element.
         */
        UIFontIcon.createFontIcon = function (fontFamily, options) {
            if (options === void 0) { options = {}; }
            options.className = Array.isArray(options.className) ? options.className : (typeof options.className === 'string' ? [options.className] : []);
            var classFamily;
            if (fontFamily === UIEnums.EFontFamily.eFont3DS) {
                classFamily = this.WUX3DSClassName;
            }
            else if (fontFamily === UIEnums.EFontFamily.eFontAwesome) {
                classFamily = this.WUXFAClassName;
            }
            options.className.unshift(classFamily);
            return UIDom.createElement('span', options);
        };
        /**
         * Gets the WUX font icon from the provided debug console severity level.
         * @public
         * @static
         * @param {ESeverity} severity - The debug console severity level.
         * @returns {Element} The created font icon element.
         */
        UIFontIcon.getWUXIconFromSeverity = function (severity) {
            var iconName = '';
            if (severity === ModelEnums.ESeverity.eInfo) {
                iconName = 'info';
            }
            else if (severity === ModelEnums.ESeverity.eDebug) {
                iconName = 'info';
            }
            else if (severity === ModelEnums.ESeverity.eWarning) {
                iconName = 'alert';
            }
            else if (severity === ModelEnums.ESeverity.eError) {
                iconName = 'attention';
            }
            else if (severity === ModelEnums.ESeverity.eSuccess) {
                iconName = 'check';
            }
            return { iconName: iconName, fontIconFamily: UIEnums.EFontFamily.eFont3DS };
        };
        /**
         * Gets the WUX font icon from the provided debug console message origin.
         * @public
         * @static
         * @param {EMessageOrigin} origin - The debug console message origin.
         * @returns {Element} The created font icon element.
         */
        UIFontIcon.getWUXIconFromMessageOrigin = function (origin) {
            var iconName = '';
            if (origin === UIEnums.EMessageOrigin.eApplication) {
                iconName = 'comment';
            }
            else if (origin === UIEnums.EMessageOrigin.eUser) {
                iconName = 'user-comment';
            }
            return { iconName: iconName, fontIconFamily: UIEnums.EFontFamily.eFont3DS };
        };
        UIFontIcon.WUXFAClassName = 'wux-ui-fa';
        UIFontIcon.WUX3DSClassName = 'wux-ui-3ds';
        return UIFontIcon;
    }());
    return UIFontIcon;
});
