import { defineStore } from 'pinia'

const TOKEN_KEY = 'colorsteel_token'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem(TOKEN_KEY) || '',
  }),
  actions: {
    setToken(t) {
      this.token = t
      if (t) localStorage.setItem(TOKEN_KEY, t)
      else localStorage.removeItem(TOKEN_KEY)
    },
    logout() {
      this.setToken('')
    },
  },
})
