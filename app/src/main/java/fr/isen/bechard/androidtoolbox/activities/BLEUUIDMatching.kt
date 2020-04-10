package fr.isen.bechard.androidtoolbox.activities

enum class BLEUUIDMatching (val uuid: String, val title: String){
    GENERIC_ACCESS("00001800-0000-1000-8000-00805f9b34fb","Accès Générique"),
    GENERIC_ATTRIBUTE("00001801-0000-1000-8000-00805f9b34fb","Attribut Générique"),
    CUSTOM_SERVICE("466c1234-f593-11e8-8eb2-f2801f1b9fd1","Service Spécifique"),
        DEVICE_NAME("00002a00-0000-1000-8000-00805f9b34fb","Nom du périphérique"),
        CUSTOM_CHARACTERISTIC_1("466c5678-f593-11e8-8eb2-f2801f1b9fd1","Charactéristic spécifique 1"),
        CUSTOM_CHARACTERISTIC_2("466c9abc-f593-11e8-8eb2-f2801f1b9fd1","Charactéristic spécifique 2"),
        UNKNOWN_SERVICE("","Inconnu");

        companion object{
        fun getBLEAttributeFromUUID(uuid: String) = values().firstOrNull{it.uuid == uuid} ?: UNKNOWN_SERVICE
    }
}
