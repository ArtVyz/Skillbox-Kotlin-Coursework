class LittleTruck : AbstractUploadedTrucks() {  // Малогрузовой (может загружаться)
    override val capacity: Int = 40
}

class MiddleTruck : AbstractUploadedTrucks() { // Среднегрузовой (может загружаться)
    override val capacity: Int = 70
}

class LargeTruck : AbstractTruck() {           // Тяжелогрузовой (не может загружаться)
    override val capacity: Int = 100
}