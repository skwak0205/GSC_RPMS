// Styles
import "../../../src/components/VSheet/VSheet.sass"; // Mixins

import BindsAttrs from '../../mixins/binds-attrs';
import Colorable from '../../mixins/colorable';
import Elevatable from '../../mixins/elevatable';
import Measurable from '../../mixins/measurable';
import Roundable from '../../mixins/roundable';
import Themeable from '../../mixins/themeable'; // Helpers

import mixins from '../../util/mixins';
/* @vue/component */

export default mixins(BindsAttrs, Colorable, Elevatable, Measurable, Roundable, Themeable).extend({
  name: 'v-sheet',
  props: {
    outlined: Boolean,
    shaped: Boolean,
    tag: {
      type: String,
      default: 'div'
    }
  },
  computed: {
    classes() {
      return {
        'v-sheet': true,
        'v-sheet--outlined': this.outlined,
        'v-sheet--shaped': this.shaped,
        ...this.themeClasses,
        ...this.elevationClasses,
        ...this.roundedClasses
      };
    },

    styles() {
      return this.measurableStyles;
    }

  },

  render(h) {
    const data = {
      class: this.classes,
      style: this.styles,
      on: this.listeners$
    };
    return h(this.tag, this.setBackgroundColor(this.color, data), this.$slots.default);
  }

});
//# sourceMappingURL=VSheet.js.map
