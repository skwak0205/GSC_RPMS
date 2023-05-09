/**
 * @author DSIS
 */

//Anonymous define here
define([], function() {
    let tool = {
        initPref(namePref, defaultValue) {
            let wdgPref = widget.getPreference(namePref);
            if (typeof wdgPref === "undefined") {
                //Create it
                widget.addPreference({
                    name: namePref,
                    type: "hidden",
                    label: namePref,
                    defaultValue: defaultValue
                });
            }
        },
        initPreferences(...prefs) {
            //Expected format : {name:string,default:object},...
            for (let i = 0; i < prefs.length; i++) {
                const objPref = prefs[i];
                this.initPref(objPref.name, objPref.default);
            }
        },
        getValueAsJSON(namePref) {
            let val = widget.getValue(namePref);
            if (val) {
                try {
                    val = JSON.parse(val.trim());
                } catch (err) {
                    console.error("JSON Parse error for pref :", namePref, " val=", val, "err", err.message);
                    console.trace();
                }
            }
            return val;
        },
        getValueAsBoolean(namePref) {
            return widget.getValue(namePref) === "true";
        }
    };
    return tool;
});