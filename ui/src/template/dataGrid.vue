<template>
  <div class="block">
    <ep-table :data="tbData" :height="height" :can-edit="false" :loading="loading">
      <template v-for="item in columns">
        <ep-table-item
          :key="item.column" :column="item.column"
          :title="item.title" :width="item.width? item.width: undefined"
          :formatter="item.formatter"></ep-table-item>
      </template>
    </ep-table>
  </div>
</template>

<script>
  import { isArray } from 'utils'

  export default {
    name: 'data-grid',

    props: {
      height: {
        type: Number,
        default: 150
      },
      api: String,
      params: Object,
      columns: Array
    },

    methods: {
      getData () {
        this.tbData = []
        this.waiting()
        this.$post(this.api, this.params).then((json) => {
          this.loading = false
          if (Array.isArray(json.data.rows)) {
            this.tbData = json.data.rows
          } else {
            this.tbData = []
          }
        }).catch(() => {
          this.loading = false
        })
      },
      waiting (toggle) {
        this.loading = toggle || true
      },
      setData (data) {
        this.loading = false
        if (isArray(data)) {
          this.tbData = data
        } else {
          throw new Error('data must been Array')
        }
      }
    },

    data () {
      return {
        loading: false,
        tbData: []
      }
    }
  }
</script>
