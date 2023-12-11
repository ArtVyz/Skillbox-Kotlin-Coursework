object FoodProducts {
    val bread = FoodTypeProduct               (1, 1000, "Хлеб")
    val milk = FoodTypeProduct                (1, 1000, "Молоко")
    val potato = FoodTypeProduct              (2, 1500, "Картошка")
    val eggs = FoodTypeProduct                (1, 2000, "Яйца")
}

object SmallSizeProducts {
    val teapot = SmallSizeTypeProduct         (2, 1000, "Чайник")
    val toaster = SmallSizeTypeProduct        (3, 1000, "Тостер")
    val iron = SmallSizeTypeProduct           (2, 1500, "Утюг")
    val grill = SmallSizeTypeProduct          (3, 1500, "Гриль")
}

object MiddleSizeProducts {
    val table = MiddleSizeTypeProduct         (6, 3000, "Стол")
    val chair = MiddleSizeTypeProduct         (4, 2000, "Стул")
    val washingMachine = MiddleSizeTypeProduct(5, 2500, "Стиральная машина")
    val stove = MiddleSizeTypeProduct         (6, 3500, "Духовая плита")
}

object LargeSizeProducts {
    val wardrobe = LargeSizeTypeProduct       (8, 4500, "Шкаф")
    val bath = LargeSizeTypeProduct           (9, 4500, "Ванна")
    val bed = LargeSizeTypeProduct            (10, 5000, "Кровать")
    val fridge = LargeSizeTypeProduct         (7, 4000, "Холодильник")
}