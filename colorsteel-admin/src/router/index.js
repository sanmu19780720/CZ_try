import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { public: true },
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/customers',
    children: [
      { path: 'customers', name: 'Customers', component: () => import('@/views/Customer.vue') },
      { path: 'products', name: 'Products', component: () => import('@/views/Product.vue') },
      { path: 'inventories', name: 'Inventories', component: () => import('@/views/Inventory.vue') },
      { path: 'purchase-orders', name: 'PurchaseOrders', component: () => import('@/views/PurchaseOrder.vue') },
      { path: 'sales-orders', name: 'SalesOrders', component: () => import('@/views/SalesOrder.vue') },
      { path: 'projects', name: 'Projects', component: () => import('@/views/Project.vue') },
      { path: 'workers', name: 'Workers', component: () => import('@/views/Worker.vue') },
      { path: 'reports', name: 'Reports', component: () => import('@/views/Report.vue') },
      { path: 'portal/site', name: 'PortalSite', component: () => import('@/views/PortalSiteConfig.vue') },
      { path: 'portal/banners', name: 'PortalBanners', component: () => import('@/views/PortalBanner.vue') },
      { path: 'portal/products', name: 'PortalProducts', component: () => import('@/views/PortalDisplayProduct.vue') },
      { path: 'portal/cases', name: 'PortalCases', component: () => import('@/views/PortalCase.vue') },
      { path: 'portal/price-cards', name: 'PortalPriceCards', component: () => import('@/views/PortalPriceCard.vue') },
      { path: 'portal/inquiries', name: 'PortalInquiries', component: () => import('@/views/PortalInquiry.vue') },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  if (to.meta.public) return true
  const store = useUserStore()
  if (!store.token) {
    return { name: 'Login', query: { redirect: to.fullPath } }
  }
  return true
})

export default router
