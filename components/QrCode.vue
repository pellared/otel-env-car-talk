<script setup>
import { computed } from 'vue'
import { renderSVG } from 'uqr'

const props = defineProps({
  value: {
    type: String,
    required: true,
  },
  label: {
    type: String,
    default: 'QR code',
  },
})

const svg = computed(() => renderSVG(props.value, { ecc: 'M', border: 4 }))
</script>

<template>
  <span class="qr-code" role="img" :aria-label="label" v-html="svg" />
</template>
