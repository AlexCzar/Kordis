# Kordis
This is a lightweight Kotlin wrapper for the Discord API. You can create discord bots with this library.  

# Installtion
with Gradle
```
repositories {
	maven { url 'https://jitpack.io' }
}
    
dependencies {
	implementation 'com.github.Tea-Ayataka:Kordis:0.1.1'
}
```
with Maven
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
    <groupId>com.github.Tea-Ayataka</groupId>
    <artifactId>Kordis</artifactId>
    <version>0.1.1</version>
</dependency>
```

# Example
```
fun main(args: Array<String>) = runBlocking {
    TestBot().start()
}

class TestBot {
    suspend fun start() {
        val client = Kordis.create {
            token = "< insert your bot token here >"
            
            // Simple Event Handler
            addHandler<UserJoinEvent> {
                println(it.member.name + " has joined")
            }
            
            // Annotation based Event Listener
            addListener(this@TestBot)
        }
    }
    
    @EventHandler
    suspend fun onMessageReceive(event: MessageReceiveEvent) {
        // Simple ping-pong
        if(event.message.content.equals("!ping", true)){
            event.message.channel.send("!pong")
        }
        
        // Sending an embedded message
        if (event.message.content == "!serverinfo") {
            event.message.channel.send {
                embed {
                    author(name = server.name)
                    field("ID", server.id)
                    field("Server created", server.timestamp.formatAsDate(), true)
                    field("Members", server.members.joinToString { it.name }, true)
                    field("Text channels", server.textChannels.joinToString { it.name })
                    field("Voice channels", server.voiceChannels.joinToString { it.name }.ifEmpty { "None" })
                    field("Emojis", server.emojis.size, true)
                    field("Roles", server.roles.joinToString { it.name }, true)
                    field("Owner", server.owner!!.mention, true)
                    field("Region", server.region.displayName, true)
                }
            }
        }
        
        // Adding a role
        if (event.message.content.equals("!member", true)) {
            val server = event.server ?: return
            val member = event.message.member ?: return

            server.roles.findByName("Member", true)?.let {
                member.addRole(it)
            }
        }
    }
```

# Dependencies
* Kotlin 1.3.0 (JVM)
* Kotlin Coroutines 1.0.1
* OkHttp 3.11.0
* Gson 2.8.5
* Java-WebSocket
