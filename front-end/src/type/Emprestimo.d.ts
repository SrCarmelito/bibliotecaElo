import { Livro } from './Livro'
import { Usuario } from './Usuario'

export type Emprestimo = {
  id?: string
  usuario?: Usuario
  livro?: Livro
  dataEmprestimo?: string
  dataDevolucao?: string
  status?: string
}
