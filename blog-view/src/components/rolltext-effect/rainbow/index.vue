<template>
  <div ref="e"></div>
</template>

<script setup>
import {computed, nextTick, onMounted, ref} from "vue";

const props = defineProps({
  rollText: Array,
  config: Object,
})
const roText  = computed(() => props.rollText)
const e = ref(null)
const aT = ref(null)
const rollText = () => {
  aT.value&&clearTimeout(aT.value);
  let a=2,s=5,l="",d=75,g=1,o=roText.value,c={i:Math.floor(Math.random()*o.length),k:0,d:'f',p:-s,t:"",z:g,y:a},b=["rgb(110,64,170)","rgb(150,61,179)","rgb(191,60,175)","rgb(228,65,157)","rgb(254,75,131)","rgb(255,94,99)","rgb(255,120,71)","rgb(251,150,51)","rgb(226,183,47)","rgb(198,214,60)","rgb(175,240,91)","rgb(127,246,88)","rgb(82,246,103)","rgb(48,239,130)","rgb(29,223,163)","rgb(26,199,194)","rgb(35,171,216)","rgb(54,140,225)","rgb(76,110,219)","rgb(96,84,200)"],t=()=>b[Math.random()*b.length|0],p=()=>String.fromCharCode(Math.random()*94+33|0),n=r=>{let n=document.createDocumentFragment();for(let i=0;r>i;i++){let l=document.createElement("span");l.style.color=t();l.className='text-base-c';l.textContent=p();n.appendChild(l);}return n;},i=()=>{let v=o[c.i];c.z?c.z--:(c.z=g,c.p<l.length?(c.p>=0&&(c.t+=l[c.p]),c.p++):c.d==='f'?c.k<v.length?c.t+=v[c.k++]:c.y?c.y--:(c.d='y',c.y=a):c.k>0?(c.t=c.t.slice(0,-1),c.k--):(c.i=(c.i+1)%o.length,c.d='f',c.y=a));e.value.textContent=c.t;e.value.appendChild(n(c.p<l.length?Math.min(s,s+c.p):Math.min(s,v.length-c.k))); aT.value=setTimeout(i,d);};i();
};

onMounted(async () => {
  await nextTick(() => {
    if(!e.value) return
    rollText()
  })
})
</script>
