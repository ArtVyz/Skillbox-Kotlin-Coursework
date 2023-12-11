abstract class Product {
    abstract val weight: Int
    abstract val loadTime: Int
    abstract val name: String
}

class FoodTypeProduct(                      // Пищевые продукты
    override val weight: Int,
    override val loadTime: Int,
    override val name: String
) : Product() {
    override fun toString(): String {
        return "Пищевые продукты"
    }
}

class SmallSizeTypeProduct(                 // Малогабаритные продукты
    override val weight: Int,
    override val loadTime: Int,
    override val name: String
) : Product() {
    override fun toString(): String {
        return "Малогабарит"
    }
}

class MiddleSizeTypeProduct(                // Среднегабариптные продукты
    override val weight: Int,
    override val loadTime: Int,
    override val name: String
) : Product() {
    override fun toString(): String {
        return "Среднегабарит"
    }
}

class LargeSizeTypeProduct(                 // Крупногабаритные продукты
    override val weight: Int,
    override val loadTime: Int,
    override val name: String
) : Product() {
    override fun toString(): String {
        return "Крупногабарит"
    }
}