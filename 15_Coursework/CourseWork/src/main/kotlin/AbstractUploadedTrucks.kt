import kotlinx.coroutines.delay

abstract class AbstractUploadedTrucks : AbstractTruck() {

    suspend fun load(productsForUpload: List<Product>, isFullOrNot: Boolean) {       // Функция загрузки

        if (productsForUpload.isEmpty()) {
            return println("Нет подходящих товаров, ожидаю товары")
        }
        if (isEmpty) {
            truckType = if (productsForUpload.first() is FoodTypeProduct) {
                "Грузовик с пищевыми товарами"
            } else {
                "Грузовик с бытовыми товарами"
            }
        }
        if (truckType == "Грузовик с пищевыми товарами") {
            println("Загрузка началась")
            var weight = 0
            val uploadIterator = productsForUpload.iterator()
            while (uploadIterator.hasNext() && weight < capacity) {
                val productForUpload = uploadIterator.next()
                if (productForUpload.weight + weight > capacity) {
                    continue
                }
                if (productForUpload is FoodTypeProduct) {
                    delay(productForUpload.loadTime.toLong())
                    truckBody.add(productForUpload)
                    weight += productForUpload.weight
                } else {
                    continue
                }
                if (!uploadIterator.hasNext() && !isFullOrNot) {
                    println("\nГрузовик загрузился не полностью, ожидаю товары (тип: $truckType, свободный вес: ${capacity - truckBody.sumOf { it.weight }})")
                }
            }
        } else {
            println("Загрузка началась")
            var weight = 0
            val uploadIterator = productsForUpload.iterator()
            while (uploadIterator.hasNext() && weight < capacity) {
                val productForUpload = uploadIterator.next()
                if (productForUpload.weight + weight > capacity) {
                    continue
                }
                if (productForUpload !is FoodTypeProduct) {
                    delay(productForUpload.loadTime.toLong())
                    truckBody.add(productForUpload)
                    weight += productForUpload.weight
                } else {
                    continue
                }
                if (!uploadIterator.hasNext() && !isFullOrNot) {
                    println("\nГрузовик загрузился не полностью, ожидаю товары (еще можно загрузить ${capacity - truckBody.sumOf { it.weight }}) ")
                }
            }
        }
        isFull = isFullOrNot
        isEmpty = truckBody.sumOf { it.weight } == 0
    }
}