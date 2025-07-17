package ru.nomad.rickandmorty.core.model

data class Character(
    val id: Int,
    val name: String,
    val status: Status,
    val species: Species,
    val type: String,
    val gender: Gender,
    val image: String,
)

enum class Status {
    ALIVE,
    DEAD,
    UNKNOWN,
}

enum class Gender {
    MALE,
    FEMALE,
    GENDERLESS,
    UNKNOWN,
}

enum class Species(
    val types: List<String>
) {
    ALIEN(
        listOf(
            "",
            "Alligator-Person",
            "Alphabetrian",
            "Animal",
            "Bepisian",
            "Bird-Person",
            "Blue ape alien",
            "Boobloosian",
            "Cat-Person",
            "Chair",
            "Cone-nippled alien",
            "Cookie",
            "Cromulon",
            "Crow",
            "Cyborg",
            "Drumbloxian",
            "Elephant-Person",
            "Ferkusian",
            "Flansian",
            "Floop Floopian",
            "Fly",
            "Garblovian",
            "Gazorpian",
            "Gear-Person",
            "Glorzo",
            "Gramuflackian",
            "Greebybobe",
            "Gromflomite",
            "Hairy alien",
            "Hivemind",
            "Interdimensional ga",
            "Korblock",
            "Krootabulan",
            "Larva alien",
            "Light bulb-Alien",
            "Lizard",
            "Lobster-Alien",
            "Memory",
            "Mexican",
            "Monogatron",
            "Morglutzian",
            "Nano Alien",
            "Normal Size Bug",
            "Numbericon",
            "Organic gun",
            "Parasite",
            "Plutonian",
            "Pripudlian",
            "Ring-nippled alien",
            "Shapeshifter",
            "Shimshamian",
            "Slartivartian",
            "Snail alien",
            "Soulless Puppet",
            "Tentacle alien",
            "Traflorkian",
            "Tumblorkian",
            "Tuskfish",
            "Unknown-nippled ali",
            "Zigerion",
            "Zombodian",
        )
    ),

    ANIMAL(
        listOf(
            "",
            "CHUD",
            "Cat",
            "Caterpillar",
            "Crow",
            "Dog",
            "Doopidoo",
            "Eel",
            "Giant Cat Monster",
            "Rat",
            "Robot-Crocodile hybrid",
            "Scrotian",
            "Sentient ant colony",
            "Shrimp",
            "Snake",
            "Soulless Puppet",
            "Squid",
            "Starfish",
            "Teddy Bear",
            "Tiger",
            "Turkey",
            "Wasp",
            "Weasel",
        )
    ),

    CRONENBERG(
        listOf(
            "",
        )
    ),

    DISEASE(
        listOf(
            "",
        )
    ),

    HUMAN(
        listOf(
            "",
            "Anime",
            "Cat controlled dead lady",
            "Clone",
            "Cronenberg",
            "Cyborg",
            "Eat shiter-Person",
            "Game",
            "Genetic experiment",
            "Grandma",
            "Half Soulless Puppet",
            "Human with a flower in his head",
            "Human with antennae",
            "Human with ants in his eyes",
            "Human with baby legs",
            "Human with giant head",
            "Human with tusks",
            "Little Human",
            "Mannie",
            "Mascot",
            "Memory",
            "Necrophiliac",
            "Old Amazons",
            "Soulless Puppet",
            "Superhuman",
            "Superhuman (Ghost trains summoner)",
            "Time God",
        )
    ),

    HUMANOID(
        listOf(
            "",
            "Amoeba-Person",
            "Bird-Person Human Mix",
            "CHUD Human Mix",
            "Chair",
            "Clay-Person",
            "Conjoined twin",
            "Corn-person",
            "Cyborg",
            "Dummy",
            "Fish-Person",
            "Giant Incest Baby",
            "Guinea Pig for the Poli",
            "Hammerhead-Person",
            "Hologram",
            "Human Gazorpian",
            "Lizard-Person",
            "Meeseeks",
            "Mega Gargantuan",
            "Microverse inhabitant",
            "Miniverse inhabitant",
            "Morty's toxic side",
            "Narnian",
            "Octopus-Person",
            "Phone",
            "Phone-Person",
            "Pizza",
            "Rick's toxic side",
            "Robot",
            "Scarecrow",
            "Summon",
            "Teenyverse inhabitant",
            "The Devil",
            "Tinymouth",
            "Trunk-Person",
            "Turkey Human Mix",
        )
    ),

    MYTHOLOGICAL_CREATURE(
        listOf(
            "",
            "Boobie buyer reptilian",
            "Centaur",
            "Demon",
            "Dragon",
            "Giant",
            "God",
            "Goddess",
            "Jellybean",
            "Leprechaun",
            "Monster",
            "Mytholog",
            "Omniscient being",
            "Sexy Aquaman",
            "Slug",
            "Stair goblin",
            "Vampire",
            "Whenwolf",
            "Zeus",
        )
    ),

    POOPYBUTTHOLE(
        listOf(
            "",
            "Soulless Puppet",
        )
    ),

    ROBOT(
        listOf(
            "",
            "Artificial Intelligence",
            "Changeformer",
            "Crow Horse",
            "Decoy",
            "Ferret Robot",
            "Gazorpian reproduction robot",
            "Human-Snake hybrid",
            "Passing Butter Robot",
            "Snake",
        )
    ),

    UNKNOWN(
        listOf(
            "Bread",
            "Hole",
            "Omniscient being",
            "Pickle",
            "Planet",
            "Self-aware arm",
            "Soulless Puppet",
            "Super Sperm Monster",
            "Toy",
        )
    );

    val displayName = name.lowercase().replaceFirstChar(Char::uppercase)
}