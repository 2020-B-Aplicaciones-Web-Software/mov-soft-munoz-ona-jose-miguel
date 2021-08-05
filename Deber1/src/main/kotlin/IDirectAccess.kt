interface IDirectAccess<T,Id> {
    fun crear(entidad:T):Unit
    fun leer(identificador:Id):T?
    fun actualizar(entidad: T)
    fun eliminar(entidad: T)
    fun listar():ArrayList<T>?
}