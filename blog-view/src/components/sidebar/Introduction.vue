  <template>
  <div >
    <div class="m-box">
      <div class="card">
        <div >
          <Image
                :src="'https://raw.githubusercontent.com/thinhh0x1l/0x1lBlog/refs/heads/main/blog-view/src/assets/img/avatar.jpg'"
              alt="Avatar"
              image-style="width: 100%; height: auto; aspect-ratio: 1/1; object-fit: cover;"
              preview
          />
        </div>
        <div class="content" >
          <div class="header">{{ introduction.name }}</div>
          <div id="rollText" v-if="introduction.rollText.length !== 0" class="m-margin-top " style="font-size: 15px;"></div>
        </div>
        <div class="extra content">
          <div class="icon-button-circle">
            <a :href="introduction.facebook" v-if="introduction.facebook" target="_blank" class="icon-button" >
              <svg style="width: 1em!important; height: 1em!important;" viewBox="0 0 15 16" xmlns="http://www.w3.org/2000/svg" fill="none"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"><path fill="#1877F2" d="M15 8a7 7 0 00-7-7 7 7 0 00-1.094 13.915v-4.892H5.13V8h1.777V6.458c0-1.754 1.045-2.724 2.644-2.724.766 0 1.567.137 1.567.137v1.723h-.883c-.87 0-1.14.54-1.14 1.093V8h1.941l-.31 2.023H9.094v4.892A7.001 7.001 0 0015 8z"></path><path fill="#ffffff" d="M10.725 10.023L11.035 8H9.094V6.687c0-.553.27-1.093 1.14-1.093h.883V3.87s-.801-.137-1.567-.137c-1.6 0-2.644.97-2.644 2.724V8H5.13v2.023h1.777v4.892a7.037 7.037 0 002.188 0v-4.892h1.63z"></path></g></svg>
            </a>
          </div>

          <!-- Instagram Icon -->
          <div class="icon-button-circle">
            <a :href="introduction.instagram" v-if="introduction.instagram" target="_blank" class="icon-button">
              <svg style="width: 1em!important;height: 1em!important;" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="2" y="2" width="28" height="28" rx="6" fill="url(#paint0_radial_87_7153)"/>
                <rect x="2" y="2" width="28" height="28" rx="6" fill="url(#paint1_radial_87_7153)"/>
                <rect x="2" y="2" width="28" height="28" rx="6" fill="url(#paint2_radial_87_7153)"/>
                <path d="M23 10.5C23 11.3284 22.3284 12 21.5 12C20.6716 12 20 11.3284 20 10.5C20 9.67157 20.6716 9 21.5 9C22.3284 9 23 9.67157 23 10.5Z" fill="white"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M16 21C18.7614 21 21 18.7614 21 16C21 13.2386 18.7614 11 16 11C13.2386 11 11 13.2386 11 16C11 18.7614 13.2386 21 16 21ZM16 19C17.6569 19 19 17.6569 19 16C19 14.3431 17.6569 13 16 13C14.3431 13 13 14.3431 13 16C13 17.6569 14.3431 19 16 19Z" fill="white"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M6 15.6C6 12.2397 6 10.5595 6.65396 9.27606C7.2292 8.14708 8.14708 7.2292 9.27606 6.65396C10.5595 6 12.2397 6 15.6 6H16.4C19.7603 6 21.4405 6 22.7239 6.65396C23.8529 7.2292 24.7708 8.14708 25.346 9.27606C26 10.5595 26 12.2397 26 15.6V16.4C26 19.7603 26 21.4405 25.346 22.7239C24.7708 23.8529 23.8529 24.7708 22.7239 25.346C21.4405 26 19.7603 26 16.4 26H15.6C12.2397 26 10.5595 26 9.27606 25.346C8.14708 24.7708 7.2292 23.8529 6.65396 22.7239C6 21.4405 6 19.7603 6 16.4V15.6ZM15.6 8H16.4C18.1132 8 19.2777 8.00156 20.1779 8.0751C21.0548 8.14674 21.5032 8.27659 21.816 8.43597C22.5686 8.81947 23.1805 9.43139 23.564 10.184C23.7234 10.4968 23.8533 10.9452 23.9249 11.8221C23.9984 12.7223 24 13.8868 24 15.6V16.4C24 18.1132 23.9984 19.2777 23.9249 20.1779C23.8533 21.0548 23.7234 21.5032 23.564 21.816C23.1805 22.5686 22.5686 23.1805 21.816 23.564C21.5032 23.7234 21.0548 23.8533 20.1779 23.9249C19.2777 23.9984 18.1132 24 16.4 24H15.6C13.8868 24 12.7223 23.9984 11.8221 23.9249C10.9452 23.8533 10.4968 23.7234 10.184 23.564C9.43139 23.1805 8.81947 22.5686 8.43597 21.816C8.27659 21.5032 8.14674 21.0548 8.0751 20.1779C8.00156 19.2777 8 18.1132 8 16.4V15.6C8 13.8868 8.00156 12.7223 8.0751 11.8221C8.14674 10.9452 8.27659 10.4968 8.43597 10.184C8.81947 9.43139 9.43139 8.81947 10.184 8.43597C10.4968 8.27659 10.9452 8.14674 11.8221 8.0751C12.7223 8.00156 13.8868 8 15.6 8Z" fill="white"/>
                <defs>
                  <radialGradient id="paint0_radial_87_7153" cx="0" cy="0" r="1" gradientUnits="userSpaceOnUse" gradientTransform="translate(12 23) rotate(-55.3758) scale(25.5196)">
                    <stop stop-color="#B13589"  offset=""/>
                    <stop offset="0.79309" stop-color="#C62F94"/>
                    <stop offset="1" stop-color="#8A3AC8"/>
                  </radialGradient>
                  <radialGradient id="paint1_radial_87_7153" cx="0" cy="0" r="1" gradientUnits="userSpaceOnUse" gradientTransform="translate(11 31) rotate(-65.1363) scale(22.5942)">
                    <stop stop-color="#E0E8B7"  offset=""/>
                    <stop offset="0.444662" stop-color="#FB8A2E"/>
                    <stop offset="0.71474" stop-color="#E2425C"/>
                    <stop offset="1" stop-color="#E2425C" stop-opacity="0"/>
                  </radialGradient>
                  <radialGradient id="paint2_radial_87_7153" cx="0" cy="0" r="1" gradientUnits="userSpaceOnUse" gradientTransform="translate(0.500002 3) rotate(-8.1301) scale(38.8909 8.31836)">
                    <stop offset="0.156701" stop-color="#406ADC"/>
                    <stop offset="0.467799" stop-color="#6A45BE"/>
                    <stop offset="1" stop-color="#6A45BE" stop-opacity="0"/>
                  </radialGradient>
                </defs>
              </svg>
            </a>
          </div>

          <div class="icon-button-circle">
            <a :href="introduction.leetCode" v-if="introduction.leetCode" target="_blank" class="ui circular icon button">
              <svg style="width: 1em!important;height: 1em!important;"  xmlns="http://www.w3.org/2000/svg" viewBox="0 0 23 26" > <path fill="#B3B1B0" d="M22 14.355c0-.742-.564-1.346-1.26-1.346H10.676c-.696 0-1.26.604-1.26 1.346s.563 1.346 1.26 1.346H20.74c.696.001 1.26-.603 1.26-1.346z"></path> <path fill="#E7A41F" d="m3.482 18.187 4.313 4.361c.973.979 2.318 1.452 3.803 1.452 1.485 0 2.83-.512 3.805-1.494l2.588-2.637c.51-.514.492-1.365-.039-1.9-.531-.535-1.375-.553-1.884-.039l-2.676 2.607c-.462.467-1.102.662-1.809.662s-1.346-.195-1.81-.662l-4.298-4.363c-.463-.467-.696-1.15-.696-1.863 0-.713.233-1.357.696-1.824l4.285-4.38c.463-.467 1.116-.645 1.822-.645s1.346.195 1.809.662l2.676 2.606c.51.515 1.354.497 1.885-.038.531-.536.549-1.387.039-1.901l-2.588-2.636a4.994 4.994 0 0 0-2.392-1.33l-.034-.007 2.447-2.503c.512-.514.494-1.366-.037-1.901-.531-.535-1.376-.552-1.887-.038l-10.018 10.1C2.509 11.458 2 12.813 2 14.311c0 1.498.509 2.896 1.482 3.876z"></path> <path fill="#070706" d="M8.115 22.814a2.109 2.109 0 0 1-.474-.361c-1.327-1.333-2.66-2.66-3.984-3.997-1.989-2.008-2.302-4.937-.786-7.32a6 6 0 0 1 .839-1.004L13.333.489c.625-.626 1.498-.652 2.079-.067.56.563.527 1.455-.078 2.066-.769.776-1.539 1.55-2.309 2.325-.041.122-.14.2-.225.287-.863.876-1.75 1.729-2.601 2.618-.111.116-.262.186-.372.305-1.423 1.423-2.863 2.83-4.266 4.272-1.135 1.167-1.097 2.938.068 4.127 1.308 1.336 2.639 2.65 3.961 3.974.067.067.136.132.204.198.468.303.474 1.25.183 1.671-.321.465-.74.75-1.333.728-.199-.006-.363-.086-.529-.179z"></path> </svg>
            </a>
          </div>

          <div class="icon-button-circle">
            <a :href="introduction.github" v-if="introduction.github" target="_blank" class="icon-button">
              <svg style="width: 1em!important;height: 1em!important;" fill="#000000" viewBox="0 0 32 32" version="1.1" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <title>github</title> <path d="M16 1.375c-8.282 0-14.996 6.714-14.996 14.996 0 6.585 4.245 12.18 10.148 14.195l0.106 0.031c0.75 0.141 1.025-0.322 1.025-0.721 0-0.356-0.012-1.3-0.019-2.549-4.171 0.905-5.051-2.012-5.051-2.012-0.288-0.925-0.878-1.685-1.653-2.184l-0.016-0.009c-1.358-0.93 0.105-0.911 0.105-0.911 0.987 0.139 1.814 0.718 2.289 1.53l0.008 0.015c0.554 0.995 1.6 1.657 2.801 1.657 0.576 0 1.116-0.152 1.582-0.419l-0.016 0.008c0.072-0.791 0.421-1.489 0.949-2.005l0.001-0.001c-3.33-0.375-6.831-1.665-6.831-7.41-0-0.027-0.001-0.058-0.001-0.089 0-1.521 0.587-2.905 1.547-3.938l-0.003 0.004c-0.203-0.542-0.321-1.168-0.321-1.821 0-0.777 0.166-1.516 0.465-2.182l-0.014 0.034s1.256-0.402 4.124 1.537c1.124-0.321 2.415-0.506 3.749-0.506s2.625 0.185 3.849 0.53l-0.1-0.024c2.849-1.939 4.105-1.537 4.105-1.537 0.285 0.642 0.451 1.39 0.451 2.177 0 0.642-0.11 1.258-0.313 1.83l0.012-0.038c0.953 1.032 1.538 2.416 1.538 3.937 0 0.031-0 0.061-0.001 0.091l0-0.005c0 5.761-3.505 7.029-6.842 7.398 0.632 0.647 1.022 1.532 1.022 2.509 0 0.093-0.004 0.186-0.011 0.278l0.001-0.012c0 2.007-0.019 3.619-0.019 4.106 0 0.394 0.262 0.862 1.031 0.712 6.028-2.029 10.292-7.629 10.292-14.226 0-8.272-6.706-14.977-14.977-14.977-0.006 0-0.013 0-0.019 0h0.001z"></path> </g></svg>
            </a>
          </div>

          <div class="icon-button-circle">
            <a :href="introduction.email" v-if="introduction.email" target="_blank" class="icon-button">
              <svg style="width: 1em!important;height: 1em!important;" fill="#000000" viewBox="0 0 19 20" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"><path d="M1.574 5.286l7.5 4.029c.252.135.578.199.906.199.328 0 .654-.064.906-.199l7.5-4.029c.489-.263.951-1.286.054-1.286H1.521c-.897 0-.435 1.023.053 1.286zm17.039 2.203l-7.727 4.027c-.34.178-.578.199-.906.199s-.566-.021-.906-.199-7.133-3.739-7.688-4.028C.996 7.284 1 7.523 1 7.707V15c0 .42.566 1 1 1h16c.434 0 1-.58 1-1V7.708c0-.184.004-.423-.387-.219z"></path></g></svg>
            </a>
          </div>

        </div>
        <div >
          <Accordion
              :multiple="false"
              v-model:value="activeIndex"

          >
            <AccordionPanel
                v-for="(item, index) in introduction.favorites"
                :key="index"
                :value="index"
            >
              <AccordionHeader >
                {{ item.title }}
                <template #toggleicon>
                  <i class="pi pi-angle-right"/>
                </template>
              </AccordionHeader>

              <AccordionContent>
                {{ item.content }}
              </AccordionContent>
            </AccordionPanel>
          </Accordion>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, nextTick, onMounted, ref, watch} from "vue";
import Panel from 'primevue/panel'
import AccordionContent from 'primevue/accordioncontent'
import AccordionHeader from 'primevue/accordionheader'
import AccordionPanel from 'primevue/accordionpanel';
import {useAppStore} from "@/store/index.ts";


const activeIndex = ref(null);
const aT = ref(null)

const store = useAppStore();
const introduction = computed(() => {
  return store.introduction})
watch(() => store.introduction?.rollText, () => {
      nextTick(() => {
        if (!document.getElementById('rollText'))return;
        rollText()
      })
    },
    {immediate:true}
)
const rollText = () => {
  aT.value&&clearTimeout(aT.value);
  let e=document.getElementById('rollText'),a=2,s=5,l="",d=75,g=1,o=introduction.value.rollText,c={i:Math.floor(Math.random()*o.length),k:0,d:'f',p:-s,t:"",z:g,y:a},b=["rgb(110,64,170)","rgb(150,61,179)","rgb(191,60,175)","rgb(228,65,157)","rgb(254,75,131)","rgb(255,94,99)","rgb(255,120,71)","rgb(251,150,51)","rgb(226,183,47)","rgb(198,214,60)","rgb(175,240,91)","rgb(127,246,88)","rgb(82,246,103)","rgb(48,239,130)","rgb(29,223,163)","rgb(26,199,194)","rgb(35,171,216)","rgb(54,140,225)","rgb(76,110,219)","rgb(96,84,200)"],t=()=>b[Math.random()*b.length|0],p=()=>String.fromCharCode(Math.random()*94+33|0),n=r=>{let n=document.createDocumentFragment();for(let i=0;r>i;i++){let l=document.createElement("span");l.style.color=t();l.className='text-base-c';l.textContent=p();n.appendChild(l);}return n;},i=()=>{let v=o[c.i];c.z?c.z--:(c.z=g,c.p<l.length?(c.p>=0&&(c.t+=l[c.p]),c.p++):c.d==='f'?c.k<v.length?c.t+=v[c.k++]:c.y?c.y--:(c.d='y',c.y=a):c.k>0?(c.t=c.t.slice(0,-1),c.k--):(c.i=(c.i+1)%o.length,c.d='f',c.y=a));e.textContent=c.t;e.appendChild(n(c.p<l.length?Math.min(s,s+c.p):Math.min(s,v.length-c.k))); aT.value=setTimeout(i,d);};i();
};
let a = null
let p = null
// onMounted(() => nextTick(() => {
//   if (!document.getElementById('rollText') || !introduction.value)return;
//     rollText()
// }))
</script>

<style scoped>

.header{
  font-weight: 700;
  font-size: 1.28571429em;
  margin-top: -.21425em;
}

.content{
  padding: 1em 0;
  border-bottom: solid 1px rgba(34, 36, 38, .1);
  text-align: center;
}

.extra{
  display: flex !important;
  justify-content: center !important;
  flex-wrap: wrap;
}



.icon-button-circle {
  width: 38px;
  height: 38px;
  border-radius: 50% !important;
  border: none  !important;
  background: rgba(75, 91, 101, 0.16) !important;
  cursor: pointer;
  margin-right: 3px;
  padding: 11px ;
  transition: background 0.3s;
}


.icon-button-circle:hover {
  background: rgba(65, 86, 106, 0.68) !important;
}

.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);

}
:deep(.p-accordionheader){
  border-radius: 0 !important;
}
:deep(.p-accordionheader .pi-angle-right){
  font-size: 17px;
}

:deep(.p-accordionheader .pi-angle-right) {
  transition: transform 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  transform: rotate(0deg);
  margin-left: 0.5rem;
}

:deep(.p-accordionpanel-active .p-accordionheader .pi-angle-right) {
  transform: rotate(90deg);
}

:deep(.p-accordioncontent){
  word-break: break-word !important;
}

:deep(.p-collapsible-enter-active ){
  animation-duration: 0.4s  !important;
}
:deep(.p-collapsible-leave-active ){
  animation-duration: 0.4s  !important;
}



</style>