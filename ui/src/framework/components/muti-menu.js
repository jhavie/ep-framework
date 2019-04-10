/**
 * Menu嵌套渲染
 * @author: Merjiezo
 * @since: 2018-04-27
 * @version: 1.0.0
 */
import mutiMenu from './muti-menu'

export default {

  name: 'muti-menu',

  components: { mutiMenu },

  props: {
    menus: Array
  },

  render () {
    let menus = this.menus
    return (
      <div>
        {
          this._l(menus, (menu, index) => [
            Array.isArray(menu.children) && menu.children.length > 0
              ? <ep-submenu icon={ menu.icon } title={ menu.name } key={ menu.name }>
                {
                  this._l(menu.children, (innerMenu, index) => [
                    Array.isArray(innerMenu.children) && innerMenu.children.length > 0
                      ? <muti-menu menus={ [ innerMenu ] }></muti-menu>
                      : <ep-menu-item index={ innerMenu.index } icon={ innerMenu.icon } title={ innerMenu.name }
                        key={ innerMenu.name } link={ innerMenu.router }>{ innerMenu.name }</ep-menu-item>
                  ])
                }
              </ep-submenu>
              : <ep-menu-item index={ menu.index } icon={ menu.icon } title={ menu.name }
                key={ menu.name } link={ menu.router }>{ menu.name }</ep-menu-item>
          ])
        }
      </div>
    )
  }
}
