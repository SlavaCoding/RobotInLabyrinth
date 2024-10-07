open class ParentTree<T>(val parent: ParentTree<T>?, val value: T){
    fun getPath(): MutableList<T> {
        val list = parent?.getPath() ?: mutableListOf()
        list.add(value)
        return list
    }
}

class EvalParentTree<T>(parent: ParentTree<T>?,
                        value: T,
                        val fromStart: Int,
                        val toPurpose: Int): ParentTree<T>(parent, value), Comparable<EvalParentTree<T>>{
    override fun compareTo(other: EvalParentTree<T>): Int {
        return (fromStart + toPurpose) - (other.fromStart+other.toPurpose)
    }
}