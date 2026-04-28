---
theme: dracula
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: true
info: |
  ## Resolución Práctica 1 - 2T
  IES Antonio Gala - 2º DAM
drawings:
  persist: false
transition: slide-left
title: Resolución Práctica 1 - CineV2
mdc: true
---



# 🔐 JWT Paso a Paso
## La Guía Definitiva - Proyecto Cine V2

<div class="mt-8 text-xl font-bold opacity-80">
  De Inseguro a Fort Knox en 6 Fases
</div>

<div class="pt-12">
  <span @click="$slidev.nav.next" class="px-2 py-1 rounded cursor-pointer hover:bg-white hover:bg-opacity-10">
    Empezar el viaje <carbon:arrow-right class="inline"/>
  </span>
</div>

---

# 🗺️ Mapa de Ruta

Construiremos la seguridad **capa por capa**, entendiendo el PORQUÉ de cada pieza.

<v-clicks>

1.  **Fase 1: Login Básico** (Sin seguridad real)
2.  **Fase 2: BCrypt** (Cifrado de contraseñas)
3.  **Fase 3: JWT** (El Token firmado)
4.  **Fase 4: El Filtro** (Interceptando peticiones)
5.  **Fase 5: UserDetailsService** (El Traductor)
6.  **Fase 6: Protección** (Cerrando las puertas)

</v-clicks>

---
layout: section
---

# 🏗️ FASE 1
## Login Básico (Inseguro)

---

# 1.1 El Objetivo Inicial

Antes de pensar en tokens o cifrados complejos, necesitamos responder a la pregunta más básica:

> **¿Existe un usuario con este email y esta contraseña en mi base de datos?**

<div class="mt-4 bg-gray-800 p-4 rounded text-sm">
Si no somos capaz de validar esto, el resto da igual.
</div>

---

# 1.2 DTOs: La Regla de Oro


Nunca, **JAMÁS**, recibas una Entidad JPA en un controlador.

<div class="grid grid-cols-2 gap-4 mt-4">
<div>

### ❌ Mal
```java
@PostMapping("/login")
public Usuario login(@RequestBody Usuario u) {
    // Expone campos internos (id, fechaAlta...)
    // Obliga a instanciar una Entidad
}
```
</div>
<div>

### ✅ Bien (DTO)
```java
// 📁 Instala en 'dtos.login'

// 📥 ENTRADA
public record LoginRequestDTO(
    String email, 
    String password
) {}

// 📤 SALIDA
public record LoginResponseDTO(
    String email, 
    String message
) {}
```
</div>
</div>


---

# 1.3 Records 📦


<div class="grid grid-cols-2 gap-8 mt-4">

<div class="bg-gray-800 p-6 rounded-lg border-2 border-purple-500 relative">
  <span class="absolute -top-3 left-4 bg-purple-500 text-white px-2 text-xs rounded font-bold">Java 16+</span>
  
  <h3 class="text-xl font-mono text-purple-300">public record Auto(String marca) {}</h3>
  
  <ul class="mt-6 space-y-3 text-sm list-none pl-0">
    <li class="flex items-center gap-2">
      ✅ <span>Constructor Canónico</span>
    </li>
    <li class="flex items-center gap-2">
      ✅ <span>Accessor automático (<code>auto.marca()</code>)</span>
    </li>
    <li class="flex items-center gap-2">
      ✅ <span><code>equals()</code> / <code>hashCode()</code></span>
    </li>
    <li class="flex items-center gap-2">
      ✅ <span><code>toString()</code></span>
    </li>
    <li class="flex items-center gap-2">
      🔒 <span>Inmutables (<b>final</b>)</span>
    </li>
  </ul>
</div>

<div class="flex flex-col justify-between">
  <div>
    <h3 class="text-green-400 font-bold mb-4 text-xl">🚀 ¿Dónde usarlos?</h3>
    <ul class="space-y-2 text-sm list-none pl-0">
      <li>📡 <b>DTOs</b> (Data Transfer Objects)</li>
      <li>📤 <b>Respuestas JSON</b> de API</li>
      <li>🔑 <b>Claves</b> en Mapas (HashMaps)</li>
      <li>⚡ <b>Streams</b> (Tuplas temporales)</li>
    </ul>
  </div>

  <div class="bg-blue-900/30 p-4 rounded border border-blue-500/50">
    <p class="text-xs mb-2 opacity-80 text-blue-200">📖 Documentación Oficial</p>
    <a href="https://docs.oracle.com/en/java/javase/17/language/records.html" target="_blank" class="flex items-center gap-2 text-blue-400 hover:text-white transition-colors duration-200">
      📄 Oracle Java SE 17: Record Classes
    </a>
  </div>
</div>

</div>

---

# 1.4 La Filosofía del Record 💭
## ¿Por qué Java necesitaba esto?

<div class="grid grid-cols-2 gap-12 items-center">

<div class="space-y-6">
  <div class="bg-red-900/20 border border-red-500/50 p-4 rounded opacity-70">
    <h3 class="text-red-300 font-bold text-sm mb-2">❌ El Viejo JavaBean (50 líneas)</h3>
    <p class="text-xs">
      Getters, Setters, Constructor vacío, Constructor lleno, equals, hashCode, toString...
      <br><b>Solo para guardar 2 Strings.</b>
    </p>
  </div>

  <div class="flex justify-center text-3xl opacity-50">⬇️</div>

  <div class="bg-green-900/20 border border-green-500/50 p-4 rounded">
    <h3 class="text-green-300 font-bold text-sm mb-2">✅ Java Record (1 línea)</h3>
    <p class="text-xs">
      Todo lo anterior, gratis y sin errores humanos.
    </p>
  </div>
</div>

<div class="space-y-4">
  <div class="flex items-start gap-3">
    <div class="bg-purple-600/20 p-2 rounded-full mt-1 border border-purple-500 text-purple-300 text-xs">🏷️</div>
    <div>
      <h3 class="font-bold text-purple-300">Tuplas con Nombre</h3>
      <p class="text-sm opacity-80">No es una lista anónima <code>[A, B]</code>.<br>Es un <code>LoginRequest</code>. Tiene significado semántico.</p>
    </div>
  </div>

  <div class="flex items-start gap-3">
    <div class="bg-blue-600/20 p-2 rounded-full mt-1 border border-blue-500 text-blue-300 text-xs">💎</div>
    <div>
      <h3 class="font-bold text-blue-300">Inmutabilidad (Safety)</h3>
      <p class="text-sm opacity-80">Nadie puede cambiar los datos por error a mitad de camino. Lo que nace, muere igual.</p>
    </div>
  </div>

  <div class="flex items-start gap-3">
    <div class="bg-yellow-600/20 p-2 rounded-full mt-1 border border-yellow-500 text-yellow-300 text-xs">📦</div>
    <div>
      <h3 class="font-bold text-yellow-300">Portador de Datos</h3>
      <p class="text-sm opacity-80">Está diseñado para <b>Mover Datos</b>, no para tener lógica de negocio compleja.</p>
    </div>
  </div>
</div>

</div>

---

# 1.5 DTOs: Mapa Mental 🧠
## ¿Qué datos viajan en cada momento?

Explicar que hay que crear 3 DTOs en el paquete `dtos`:

<div class="grid grid-cols-3 gap-4 mt-8">

<!-- 1. LOGIN REQUEST -->
<div class="bg-blue-900/20 p-4 rounded border border-blue-500/50">
  <div class="flex items-center gap-2 mb-2">
    <div class="bg-blue-500 text-white w-6 h-6 rounded-full flex items-center justify-center font-bold text-xs">1</div>
    <h3 class="font-bold text-blue-300 text-sm">LoginRequestDTO</h3>
  </div>
  <p class="text-xs opacity-70 mb-2">👉 Entrada del Login</p>
  <pre class="text-[10px] bg-black/50 p-2 rounded text-blue-200">
record LoginRequestDTO(
  String email,
  String password
) {}</pre>
</div>

<!-- 2. LOGIN RESPONSE -->
<div class="bg-green-900/20 p-4 rounded border border-green-500/50">
  <div class="flex items-center gap-2 mb-2">
    <div class="bg-green-500 text-white w-6 h-6 rounded-full flex items-center justify-center font-bold text-xs">2</div>
    <h3 class="font-bold text-green-300 text-sm">LoginResponseDTO</h3>
  </div>
  <p class="text-xs opacity-70 mb-2">👉 Salida del Login</p>
  <pre class="text-[10px] bg-black/50 p-2 rounded text-green-200">
record LoginResponseDTO(
  String email,
  String message,
  String token
) {}</pre>
</div>

<!-- 3. REGISTER REQUEST -->
<div class="bg-purple-900/20 p-4 rounded border border-purple-500/50">
  <div class="flex items-center gap-2 mb-2">
    <div class="bg-purple-500 text-white w-6 h-6 rounded-full flex items-center justify-center font-bold text-xs">3</div>
    <h3 class="font-bold text-purple-300 text-sm">RegisterRequestDTO</h3>
  </div>
  <p class="text-xs opacity-70 mb-2">👉 Entrada del Registro</p>
  <pre class="text-[10px] bg-black/50 p-2 rounded text-purple-200">
record RegisterRequestDTO(
  String email,
  String password
) {}</pre>
</div>

</div>

---

# 1.6 Servicio: Login en Texto Plano

En `UsuarioService.java`:

```java
public LoginResponseDTO login(LoginRequestDTO request) {
    // 1. Buscar por email
    Usuario usuario = repository.findByEmail(request.email())
        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

    // 2. Comparar contraseña (ERROR GRAVE DE SEGURIDAD AQUÍ)
    if (!usuario.getPassword().equals(request.password())) {
        // throw new BadCredentialsException("Contraseña incorrecta");
        throw new RuntimeException("Contraseña incorrecta"); // Cambiaremos a BadCredentialsException con Spring Security
    }

    // 3. Devolver DTO (NO entidad)
    return new LoginResponseDTO(
            usuario.getEmail(),
            "Login exitoso (Inseguro)"
    );
}
```

<v-click>
<div class="absolute bottom-10 right-10 bg-red-600 text-white p-4 rounded-lg transform rotate-[-5deg]">
  💀 ¡ESTO ES INSEGURO! 💀
</div>
</v-click>

---

# 1.7 El Controlador: AuthController 🌐

Exponemos la lógica al mundo exterior mediante una API REST.

```java
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(usuarioService.login(loginRequest));
    }
}
```

---

# 1.8 Flujo de una Petición Exitosa ✅

<div class="flex flex-col gap-4 mt-6">

<!-- ROW 1: The Request -->
<div class="flex items-center justify-center gap-4">

<div class="bg-gray-700 p-3 rounded-lg border border-gray-500 flex flex-col items-center w-32">
<span class="text-2xl">👤</span>
<span class="font-bold text-sm">Cliente</span>
</div>

<div class="text-2xl animate-pulse">➡️</div>

<div class="bg-blue-900/40 p-3 rounded-lg border border-blue-500 flex flex-col items-center w-40">
<span class="text-xs font-mono mb-1 text-blue-300">LoginRequestDTO</span>
<div class="font-bold text-sm">AuthController</div>
</div>

<div class="text-2xl animate-pulse">➡️</div>

<div class="bg-purple-900/40 p-3 rounded-lg border border-purple-500 flex flex-col items-center w-40">
<span class="text-xs font-mono mb-1 text-purple-300">UsuarioService</span>
<div class="text-[10px] text-left w-full pl-2 border-l-2 border-purple-400 opacity-80 mt-1">
1. Busca Email<br>
2. Chequea Pass<br>
3. Crea DTO
</div>
</div>

</div>

<!-- ROW 2: The Database Interaction -->
<div class="flex items-center justify-center gap-4">
<div class="w-32"></div> <!-- Spacer -->
<div class="w-40"></div> <!-- Spacer -->

<div class="text-2xl rotate-90 transform">⬇️</div>
</div>

<div class="flex items-center justify-center gap-4 -mt-2">
<div class="w-32"></div> <!-- Spacer -->
<div class="w-40"></div> <!-- Spacer -->

<div class="bg-yellow-900/40 p-3 rounded-lg border border-yellow-500 flex items-center justify-center gap-2 w-40">
<span class="text-2xl">🗄️</span>
<span class="font-bold text-sm text-yellow-200">Repository</span>
</div>
</div>

</div>

<div class="grid grid-cols-2 gap-4 mt-4 text-sm">
<div class="bg-gray-800 p-3 rounded border-l-4 border-green-500">
  <h3 class="font-bold text-green-400">✅ 200 OK</h3>
  Si el servicio devuelve un usuario, todo ha ido bien.
</div>
<div class="bg-gray-800 p-3 rounded border-l-4 border-red-500">
  <h3 class="font-bold text-red-400">❌ Errores (401/404)</h3>
  Si el servicio lanza excepción, nuestro <code>GlobalExceptionHandler</code> las captura y devuelve JSON de error.
</div>
</div>

---

# 1.9 Estado de la Cuestión

<div class="grid grid-cols-2 gap-8 mt-4">

  <div class="bg-green-900/10 border border-green-500/50 rounded-xl p-6 relative overflow-hidden group hover:bg-green-900/20 transition-all">
    <div class="absolute right-[-20px] top-[-20px] text-6xl opacity-10 rotate-12 group-hover:rotate-0 transition-all">✅</div>
    <h3 class="text-xl font-bold text-green-400 mb-4 border-b border-green-500/30 pb-2">Lo que funciona</h3>
    <ul class="space-y-3">
      <li class="flex items-center gap-3">
        <div class="w-1.5 h-1.5 rounded-full bg-green-400"></div>
        <span class="opacity-90">El usuario puede loguearse en el sistema.</span>
      </li>
      <li class="flex items-center gap-3">
        <div class="w-1.5 h-1.5 rounded-full bg-green-400"></div>
        <span class="opacity-90">Validamos reglas de negocio (existe/no existe).</span>
      </li>
      <li class="flex items-center gap-3">
        <div class="w-1.5 h-1.5 rounded-full bg-green-400"></div>
        <span class="opacity-90">Devolvemos códigos HTTP correctos (200, 401, 404).</span>
      </li>
    </ul>
  </div>

  <div class="bg-red-900/10 border border-red-500/50 rounded-xl p-6 relative overflow-hidden group hover:bg-red-900/20 transition-all">
    <div class="absolute right-[-20px] top-[-20px] text-6xl opacity-10 rotate-12 group-hover:rotate-0 transition-all">❌</div>
    <h3 class="text-xl font-bold text-red-400 mb-4 border-b border-red-500/30 pb-2">Lo que falta</h3>
    <ul class="space-y-3">
      <li class="flex items-start gap-3">
        <div class="w-1.5 h-1.5 rounded-full bg-red-400 mt-2"></div>
        <span class="opacity-90">Las contraseñas están <b>EN CLARO</b> en la Base de Datos.</span>
      </li>
      <li class="flex items-start gap-3">
        <div class="w-1.5 h-1.5 rounded-full bg-red-400 mt-2"></div>
        <span class="opacity-90">Si roban la BD, tienen acceso a <b>TODO</b>.</span>
      </li>
      <li class="flex items-start gap-3">
        <div class="w-1.5 h-1.5 rounded-full bg-red-400 mt-2"></div>
        <span class="opacity-90">Exposición de seguridad crítica.</span>
      </li>
    </ul>
  </div>

</div>

---
layout: section
---

# 🔒 FASE 2
## BCrypt (Seguridad Real en BD)

---

# 2.1 El Problema: Texto Plano y Cifrado Reversible 🚩
## ¿Por qué no basta con ocultarlo?

<div class="grid grid-cols-2 gap-6 mt-4">

<div class="opacity-80">
<h3 class="text-xl font-bold text-red-400 mb-2">1. Texto Plano</h3>
<p class="text-sm mb-2">Guardar `1234` en la BD sin transformar.</p>
<ul class="text-xs space-y-2 list-disc pl-4 opacity-70">
<li>Cualquier admin de BD puede verla.</li>
<li>Si sufres un ataque (SQLi), roban las credenciales.</li>
<li>Responsabilidad legal total.</li>
</ul>
</div>

<div class="bg-blue-900/20 p-4 rounded-xl border border-blue-500/50">
<div class="flex items-center gap-2 mb-2">
<div class="text-xl">↔️</div>
<h3 class="text-lg font-bold text-blue-300">Cifrado (Encryption)</h3>
</div>
<ul class="space-y-2 text-xs">
<li>✅ <b>Bidireccional:</b> Se puede revertir si tienes la clave (Key).</li>
<li>❌ <b>El Riesgo:</b> Si el atacante roba la clave (o un empleado descontento la tiene), descifra TODAS las contraseñas al instante.</li>
</ul>
<div class="mt-2 bg-black/40 p-2 rounded text-[10px] font-mono text-center">
"Hola" + 🔑 -> "Xy7#b" -> "Hola"
</div>
</div>

</div>

<div class="mt-8 text-center">
<a href="https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html" target="_blank" class="text-xs opacity-50 hover:opacity-100 transition-opacity border-b border-gray-500 pb-0.5">
🔗 OWASP Password Storage Cheat Sheet
</a>
</div>

---

# 2.2 La Solución Real: Hashing 🔐
## Matemáticas sin retorno

<div class="grid grid-cols-2 gap-8 mt-6">

<div class="bg-green-900/20 p-6 rounded-xl border border-green-500/50">
<div class="flex items-center gap-3 mb-4">
<div class="text-3xl">➡️</div>
<h3 class="text-xl font-bold text-green-300">Hashing</h3>
</div>
<ul class="space-y-2 text-sm">
<li>🛑 <b>Unidireccional:</b> NO tiene vuelta atrás.</li>
<li>🚫 No existe "descifrar" un hash.</li>
<li>🔒 <i>Ejemplo:</i> Contraseñas. Solo necesito saber si coinciden.</li>
</ul>
<div class="mt-4 bg-black/40 p-2 rounded text-xs font-mono text-center">
"Hola" -> "5d4140..." -> ❓
</div>
</div>

<div class="bg-yellow-900/20 p-4 rounded-lg border border-yellow-500/50 flex flex-col justify-center">
<div class="flex items-center gap-2 mb-2">
<div class="text-2xl">🍹</div>
<h3 class="font-bold text-yellow-200">Analogía del Batido</h3>
</div>
<p class="text-sm opacity-90 leading-relaxed">
Puedes hacer un batido de una fresa (Hash).<br><br>
<b>Pero no puedes sacar la fresa del batido.</b>
</p>
<div class="mt-4 border-t border-yellow-500/30 pt-2">
<p class="text-xs opacity-70">
Si vuelvo a batir la misma fresa, obtengo el mismo batido.<br>
Así verificamos el login.
</p>
</div>
</div>

</div>


---

# 2.3 Anatomía de BCrypt 🧬
## ¿Qué significan esos caracteres raros?

Cuando ves esto en la base de datos:
<div class="text-center font-mono text-xl my-6 bg-black/50 p-4 rounded text-green-400 tracking-wider">
$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWrn96p...
</div>

No es cadena aleatoria. Tiene una estructura precisa:

<div class="grid grid-cols-3 gap-4 mt-8">

<div class="text-center">
<div class="text-3xl mb-2">🏷️</div>
<h3 class="font-bold text-purple-300 mb-1">$2a$</h3>
<span class="text-xs uppercase bg-purple-900/50 px-2 py-1 rounded">Versión</span>
<p class="text-xs mt-2 opacity-70">Identificador del algoritmo (BCrypt).</p>
</div>

<div class="text-center border-x border-gray-700 px-4">
<div class="text-3xl mb-2">⏱️</div>
<h3 class="font-bold text-blue-300 mb-1">10</h3>
<span class="text-xs uppercase bg-blue-900/50 px-2 py-1 rounded">Cost Factor</span>
<p class="text-xs mt-2 opacity-70">2^10 iteraciones. Define la lentitud.</p>
</div>

<div class="text-center">
<div class="text-3xl mb-2">🧂</div>
<h3 class="font-bold text-green-300 mb-1">...EixZa...</h3>
<span class="text-xs uppercase bg-green-900/50 px-2 py-1 rounded">Salt (22 chars)</span>
<p class="text-xs mt-2 opacity-70">Aleatoriedad pura generada por usuario.</p>
</div>

</div>

<div class="mt-8 text-center text-sm opacity-60">
Los últimos 31 caracteres son el hash real resultante.
</div>

---

# 2.4 El Poder del SALT 🧂
## ¿Por qué dos contraseñas iguales tienen Hash distinto?

Si Pepe y Maria tienen la clave `123456`:

<div class="grid grid-cols-2 gap-8 mt-6">

<div class="opacity-50 grayscale">
<h3 class="font-bold text-center mb-2">❌ SHA-256 (Sin Salt / Salt fijo)</h3>
<div class="bg-gray-800 p-3 rounded text-xs font-mono">
Pepe: <b>8d969e...</b><br>
Maria: <b>8d969e...</b>
</div>
<p class="text-red-400 text-xs mt-2 text-center">
Peligro: Rainbow Tables buscan "8d969e" y saben que es "123456".
</p>
</div>

<div class="relative">
<div class="absolute -top-3 -right-3 text-4xl animate-bounce">🛡️</div>
<h3 class="font-bold text-center mb-2 text-green-400">✅ BCrypt (Auto-Salt)</h3>
<div class="bg-gray-800 p-3 rounded text-xs font-mono border border-green-500">
Pepe: $2a$10$<b>Xy9Z...</b> (Salt A) -> a8f9...<br>
Maria: $2a$10$<b>Bk2L...</b> (Salt B) -> c4d2...
</div>
<p class="text-green-300 text-xs mt-2 text-center">
Misma password, hashes radicalmente distintos.
<br>¡Rompe las Rainbow Tables!
</p>
</div>

</div>

> **Conclusión:** El SALT es un valor aleatorio que se **concatena** a la contraseña antes de hashear. BCrypt lo genera y lo guarda dentro del propio string final.

---

# 2.5 Cost Factor (Work Factor) 🏋️
## La defensa contra la Fuerza Bruta

La velocidad es enemiga de la seguridad en contraseñas.
Si un atacante puede probar 1.000.000 claves/segundo, descubrirá la clave.

**El Cost Factor ($10$)** hace que el algoritmo sea **intencionalmente LENTO**.

- Cost 10: ~100ms por verificación.
- Si el atacante quiere probar todo el diccionario, tardará años en lugar de minutos.

<div class="bg-gray-800 p-4 rounded mt-6 border-l-4 border-orange-500">
<h3 class="font-bold text-orange-400">⚖️ El Equilibrio</h3>
<ul class="text-sm mt-2 space-y-1">
<li><b>Muy bajo (8):</b> Inseguro (demasiado rápido de romper).</li>
<li><b>Muy alto (16):</b> Mal UX (el usuario espera 5 segundos para loguear).</li>
<li><b>Estándar (10-12):</b> Perfecto (seguro y usuario no lo nota).</li>
</ul>
</div>

---

# 2.6 Flujo: Registro vs Login 🔄

<div class="grid grid-cols-2 gap-6 mt-2">

<div>
<h3 class="text-center font-bold text-purple-300 mb-2">1️⃣ REGISTRO</h3>
<div class="flex flex-col gap-2 text-xs">
<div class="bg-gray-700 p-2 rounded text-center">Usuario envía "hunter2"</div>
<div class="text-center">⬇️</div>
<div class="bg-purple-900/40 p-2 rounded border border-purple-500 text-center">
<b>BCrypt:</b> <br>
1. Genera Salt aleatorio<br>
2. Hash("hunter2" + Salt)<br>
3. Resultado: <code>$2a$10$Salt...Hash</code>
</div>
<div class="text-center">⬇️</div>
<div class="bg-yellow-900/40 p-2 rounded border border-yellow-500 text-center">
Guardar en BD
</div>
</div>
</div>

<div>
<h3 class="text-center font-bold text-green-300 mb-2">2️⃣ LOGIN</h3>
<div class="flex flex-col gap-2 text-xs">
<div class="bg-gray-700 p-2 rounded text-center">Usuario envía "hunter2"</div>
<div class="text-center">⬇️</div>
<div class="bg-green-900/40 p-2 rounded border border-green-500 text-center">
<b>passwordEncoder.matches():</b><br>
1. Lee el Salt del Hash de la BD<br>
2. Hash("hunter2" + SaltLeído)<br>
3. ¿Coincide con el de la BD?
</div>
<div class="text-center">⬇️</div>
<div class="bg-blue-600 p-2 rounded text-center font-bold">
✅ TRUE / ❌ FALSE
</div>
</div>
</div>
</div>

---



# 2.7 Paso 1: Dependencia 📦

Añadimos `spring-boot-starter-security`.

<div class="mt-4 bg-purple-900/30 border border-purple-500/50 p-4 rounded text-sm mb-4">
<h3 class="font-bold text-purple-300 mb-2">🎁 ¿Qué nos regala esta dependencia?</h3>
<ul class="list-disc pl-4 space-y-1">
<li><b>Auto-Configuración</b>: Cierra todos los endpoints por defecto.</li>
<li><b>Filtros de Seguridad</b>: Crea la cadena de filtros (SecurityFilterChain).</li>
<li><b>Cifrado</b>: Nos da acceso a <code>BCryptPasswordEncoder</code>.</li>
</ul>
</div>

<div class="bg-red-900/30 border border-red-500/50 p-4 rounded text-sm">
<h3 class="font-bold text-red-300 mb-2">🚧 ¡Cuidado! Efecto Inmediato</h3>
<p>En cuanto añades esto y reinicias, <b>TODAS tus rutas devuelve 401 Unauthorized</b>.</p>
<p class="opacity-80 text-xs mt-1">Spring protege todo por defecto hasta que le digas lo contrario.</p>
</div>

---

# 2.8 Paso 2: Configuración (Beans) ⚙️

Es buena práctica crear un paquete `security` para organizar estas clases.
<br>📁 `src/main/java/.../security/PasswordConfig.java`

<div class="grid grid-cols-2 gap-4 mt-2">

<div>

### 1️⃣ Dependencia (pom.xml)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
</div>

<div>

### 2️⃣ Configuración (Java)
```java
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
</div>

</div>

<div class="mt-4 text-center text-sm opacity-70">
Al definir este <code>@Bean</code>, podremos inyectar <code>PasswordEncoder</code> en cualquier Servicio.
</div>

---

# 2.9 Paso 3: Implementación 🛠️
## Servicio y Controlador (Registro + Login)

<div class="grid grid-cols-2 gap-4 mt-2 text-xs">

<div class="h-[380px] overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-purple-500 scrollbar-track-gray-800 border border-purple-500/30 rounded p-2">
<h3 class="font-bold text-purple-300 mb-2 sticky top-0 bg-[#121212] z-10 pb-1 border-b border-purple-500/30">UsuarioService.java</h3>

```java
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder; // Inyectado

    // 🔹 REGISTRO
    public void register(RegisterRequestDTO req) {
        Usuario u = new Usuario();
        u.setEmail(req.email());
        // 🔐 CIFRAR ANTES DE GUARDAR
        u.setPassword(encoder.encode(req.password())); 
        u.setRol("USER");
        repo.save(u);
    }

    // 🔹 LOGIN
    public LoginResponseDTO login(LoginRequestDTO req) {
        Usuario u = repo.findByEmail(req.email())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); // OJO: Usar BadCredentialsException después

        // 🔐 COMPARAR (Raw vs Hash)
        if (!encoder.matches(req.password(), u.getPassword())) {
             throw new RuntimeException("Credenciales incorrectas");
        }

        return new LoginResponseDTO(u.getEmail(), "Login OK");
    }
}
```
</div>

<div class="h-[380px] overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-green-500 scrollbar-track-gray-800 border border-green-500/30 rounded p-2">
<h3 class="font-bold text-green-300 mb-2 sticky top-0 bg-[#121212] z-10 pb-1 border-b border-green-500/30">AuthController.java</h3>

```java
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @RequestBody RegisterRequestDTO req
    ) {
        service.register(req);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new RegisterResponseDTO(req.email(), "Creado"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO req
    ) {
        return ResponseEntity.ok(service.login(req));
    }
}
```
</div>

</div>

---

# 2.10 Paso 4: Abrir las Puertas 🔓
## SecurityConfig

Aunque tengamos todo el código listo, **si probamos ahora fallará (401 Unauthorized)**.
Spring Security ha cerrado todo por defecto. Tenemos que decirle qué rutas son públicas.

<div class="flex justify-center mt-4">
<div class="bg-gray-800 p-4 rounded-lg border border-gray-600 max-w-2xl w-full text-sm max-h-80 overflow-y-auto scrollbar-thin scrollbar-thumb-gray-500 scrollbar-track-gray-800">

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Necesario para APIs REST (Stateless)
            .authorizeHttpRequests(auth -> auth
                // 🔓 PERMITIR ACCESO PÚBLICO A /auth/** (Registro y Login)
                .requestMatchers("/auth/**").permitAll()
                // 🔒 TODO LO DEMÁS: Requiere autenticación
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}
```
</div>
</div>

<div class="mt-6 text-center">
<span class="bg-blue-600 text-white px-4 py-2 rounded-full text-sm font-bold shadow-lg animate-bounce">
🚀 ¡AHORA SÍ! Ya puedes probar el Registro y Login en Postman.
</span>
</div>

---
layout: section
---

# 🎫 FASE 3
## JWT (JSON Web Token)

---

# 3.1 El Concepto: "Tu Carné de Identidad Digital" 🪪

Imagina que JWT es como un **DNI o Pasaporte** que te expide el servidor.

<div class="grid grid-cols-2 gap-8 mt-6">

<div class="space-y-4">
  <div class="bg-gray-800 p-4 rounded border-l-4 border-blue-500">
    <h3 class="font-bold text-blue-400">1. Login Exitoso</h3>
    <p class="text-sm">El servidor comprueba tu contraseña y user, y te <b>imprime</b> un carné (Token) firmado.</p>
  </div>
  
  <div class="flex justify-center text-2xl opacity-50">⬇️</div>

  <div class="bg-gray-800 p-4 rounded border-l-4 border-green-500">
    <h3 class="font-bold text-green-400">2. Peticiones Futuras</h3>
    <p class="text-sm">Para entrar al club, ya no dices tu contraseña. Solo <b>enseñas el carné</b>.</p>
  </div>
</div>

<div class="flex flex-col items-center justify-center bg-gray-900/50 p-4 rounded-lg border border-gray-700">
  <div class="text-6xl mb-4">🎫</div>
  <div class="text-center">
    <h3 class="font-mono text-yellow-300 text-lg">eyJhbGciOiJIUzI...</h3>
    <p class="text-xs text-gray-400 mt-2">
      "Este usuario es PEPE<br>
      Es ADMIN<br>
      Válido hasta las 18:00"<br>
      <span class="text-red-400 font-bold">(Firma del Servidor)</span>
    </p>
  </div>
</div>

</div>

---

# 3.2 Anatomía de un JWT

Un string largo con 3 partes separadas por puntos `.`:
`aaaaaa.bbbbbb.cccccc`

1.  **Header**: "Soy un JWT y uso el algoritmo HS256".
2.  **Payload (Cuerpo)**: "Me llamo pepe@gmail.com, soy ADMIN y caduco en 1 hora".
3.  **Signature (Firma)**: Sello de lacre criptográfico.

---

# 3.3 La Firma (Signature)

Esto es lo vital.
La firma se calcula así:

`HMACSHA256(Header + Payload, SECRETO_DEL_SERVIDOR)`

Si el usuario (hacker) intenta cambiar "soy ADMIN" en el Payload:
1.  La firma matemática ya no coincidirá.
2.  El servidor rechazará el token.

> **Solo el servidor (que tiene el SECRETO) puede crear tokens válidos.**

---

# 3.4 La Herramienta: JJWT 🧰

Para trabajar con tokens, usamos la librería **JJWT** (Java JWT).

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

---

# 3.5 Arquitectura de Seguridad 🏛️
## El Trío Dinámico

Todo lo relacionado con seguridad debe ir en su propio paquete:
<br>📂 `src/main/java/.../security/`

<div class="grid grid-cols-3 gap-6 mt-8">

<div class="bg-red-900/20 p-4 rounded border border-red-500/50 flex flex-col items-center text-center">
  <div class="text-3xl mb-2">👮‍♂️</div>
  <h3 class="font-bold text-red-300">SecurityConfig</h3>
  <p class="text-xs opacity-70 mt-2"><b>El Portero.</b><br>Decide quién entra y quién no (Rutas públicas vs privadas).</p>
</div>

<div class="bg-blue-900/20 p-4 rounded border border-blue-500/50 flex flex-col items-center text-center">
  <div class="text-3xl mb-2">🔐</div>
  <h3 class="font-bold text-blue-300">PasswordConfig</h3>
  <p class="text-xs opacity-70 mt-2"><b>El Candado.</b><br>Configura BCrypt para cifrar las contraseñas.</p>
</div>

<div class="bg-green-900/20 p-4 rounded border border-green-500/50 flex flex-col items-center text-center relative overflow-visible">
  <div class="absolute -top-3 -right-3 text-xl animate-bounce">🆕</div>
  <div class="text-3xl mb-2">🏭</div>
  <h3 class="font-bold text-green-300">JwtUtil</h3>
  <p class="text-xs opacity-70 mt-2"><b>La Fábrica.</b><br>Genera, firma y valida los Tokens (DNI).</p>
</div>

</div>

---

# 3.6 Preparando el Terreno: Properties ⚙️

En `application.properties`, definimos la estructura, **pero no el secreto**.

```properties
# 1. El secreto se inyecta desde una Variable de Entorno
# Nunca escribas la clave real aquí
jwt.secret=${JWT_SECRET}

# 2. Tiempo de expiración (1 hora)
jwt.expiration=3600000
```

<div class="grid grid-cols-2 gap-4 mt-6">
  <div class="bg-blue-900/30 border border-blue-500/50 p-4 rounded text-sm">
    <h3 class="font-bold text-blue-300 mb-2">💻 En tu IDE (IntelliJ / VSCode)</h3>
    <p>Tienes que ir a <b>Edit Configurations</b> -> <b>Environment Variables</b> y añadir:</p>
    <code class="block mt-2 bg-black p-2 rounded text-xs">JWT_SECRET=MiClaveSuperSecreta12345...</code>
  </div>

  <div class="bg-yellow-900/30 border border-yellow-500/50 p-4 rounded text-sm">
    <h3 class="font-bold text-yellow-300 mb-2">⚠️ Por qué hacemos esto?</h3>
    <p>Para no subir accidentalmente nuestras contraseñas a GitHub.</p>
  </div>
</div>

---

# 3.7 JwtUtil: La Implementación 🛠️

<div class="h-[430px] overflow-y-auto pr-4 scrollbar-thin scrollbar-thumb-purple-500 scrollbar-track-gray-800">

```java
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret; // "MiClave..." (String)

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey key; // Objeto Criptográfico Real

    // ⚡ SE EJECUTA DESPUÉS DEL CONSTRUCTOR
    @PostConstruct
    public void init() {
        // Transformamos la password de texto a una llave HMAC-SHA compatible
        // ¿Por qué aquí? Porque en el constructor 'secret' aún es null.
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
            .subject(usuario.getEmail())     
            .claim("role", usuario.getRol()) 
            .issuedAt(new Date())            
            .expiration(new Date(System.currentTimeMillis() + expirationTime)) 
            .signWith(key)                   
            .compact();                      
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false; // Firma inválida o expirado
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token).getPayload().getSubject();
    }
}
```

</div>


---

# 3.8 El Builder Pattern (generateToken) 🏗️
## ¿Cómo se fabrica un Token paso a paso?

<div class="flex flex-col gap-4 mt-6">

<div class="flex items-center gap-4 text-sm">
  <div class="bg-gray-700 font-mono px-2 py-1 rounded w-32 text-right">.builder()</div>
  <div class="opacity-50">➡️</div>
  <div>Abre la caja vacía del Token.</div>
</div>

<div class="flex items-center gap-4 text-sm">
  <div class="bg-purple-900/50 font-mono px-2 py-1 rounded w-32 text-right text-purple-300">.subject()</div>
  <div class="opacity-50">➡️</div>
  <div>"Este DNI pertenece a <b>pepe@gmail.com</b>"</div>
</div>

<div class="flex items-center gap-4 text-sm">
  <div class="bg-blue-900/50 font-mono px-2 py-1 rounded w-32 text-right text-blue-300">.claim()</div>
  <div class="opacity-50">➡️</div>
  <div>Añade datos extra: <code>"role": "USER"</code></div>
</div>

<div class="flex items-center gap-4 text-sm">
  <div class="bg-yellow-900/50 font-mono px-2 py-1 rounded w-32 text-right text-yellow-300">.expiration()</div>
  <div class="opacity-50">➡️</div>
  <div>"Caduca en 1 hora". (Si vienes mañana, no entras).</div>
</div>

<div class="flex items-center gap-4 text-sm">
  <div class="bg-red-900/50 font-mono px-2 py-1 rounded w-32 text-right text-red-300">.signWith(key)</div>
  <div class="opacity-50">➡️</div>
  <div class="font-bold text-red-400">🔥 EL PASO CRÍTICO.</div>
  <div class="text-xs opacity-70">Pone el sello de lacre con tu SECRETO. Sin esto, el token es papel mojado.</div>
</div>

<div class="flex items-center gap-4 text-sm">
  <div class="bg-green-900/50 font-mono px-2 py-1 rounded w-32 text-right text-green-300">.compact()</div>
  <div class="opacity-50">➡️</div>
  <div>Cierra la caja y te la da comprimida (String <code>ey...</code>).</div>
</div>

</div>

---

# 3.9 Modificando el Login (UsuarioService)

Ahora el login no devuelve un `Usuario`, devuelve un `LoginResponseDTO` con el token.

```java
public LoginResponseDTO login(LoginRequest req) {
    Usuario u = repo.findByEmail(req.email())
            .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));
    
    if (!passwordEncoder.matches(req.password(), u.getPassword())) 
        throw new BadCredentialsException("Contraseña incorrecta");

    // Generamos el pase VIP (Token)
    String token = jwtUtil.generateToken(u);

    // Devolvemos DTO con todo
    return new LoginResponseDTO(
        u.getEmail(), 
        "Login exitoso", 
        token
    );
}
```

---

# 3.10 Checklist de Seguridad ✅

¿Qué hemos conseguido hasta ahora?

<div class="grid grid-cols-2 gap-4 mt-6">
  <div class="space-y-2">
    <div class="flex items-center gap-3 bg-green-900/20 p-2 rounded border border-green-500/30">
      <div class="bg-green-500 text-black font-bold w-6 h-6 rounded-full flex items-center justify-center">1</div>
      <span>Usuarios en Base de Datos</span>
    </div>
    <div class="flex items-center gap-3 bg-green-900/20 p-2 rounded border border-green-500/30">
      <div class="bg-green-500 text-black font-bold w-6 h-6 rounded-full flex items-center justify-center">2</div>
      <span>Contraseñas Cifradas (BCrypt)</span>
    </div>
    <div class="flex items-center gap-3 bg-green-900/20 p-2 rounded border border-green-500/30">
      <div class="bg-green-500 text-black font-bold w-6 h-6 rounded-full flex items-center justify-center">3</div>
      <span>Generación de Tokens (JWT)</span>
    </div>
  </div>

  <div class="space-y-2 opacity-50">
    <div class="flex items-center gap-3 bg-red-900/10 p-2 rounded border border-red-500/10">
      <div class="bg-gray-500 text-black font-bold w-6 h-6 rounded-full flex items-center justify-center">4</div>
      <span>Validación Automática (Filtro)</span>
    </div>
    <div class="flex items-center gap-3 bg-red-900/10 p-2 rounded border border-red-500/10">
      <div class="bg-gray-500 text-black font-bold w-6 h-6 rounded-full flex items-center justify-center">5</div>
      <span>Protección de Rutas</span>
    </div>
  </div>
</div>

---

# 🕵️ Misión de 5 Minutos: Decodificar
## ¿Qué esconde realmente el token?

<div class="mt-8 bg-[#282a36] p-6 rounded-xl border border-[#bd93f9]/50 shadow-2xl relative overflow-hidden">
  <div class="absolute -top-10 -right-10 w-40 h-40 bg-[#bd93f9]/20 rounded-full blur-3xl"></div>
  <div class="grid grid-cols-2 gap-8 relative z-10">
    <div class="space-y-4">
      <div class="flex items-center gap-4 bg-[#44475a] p-3 rounded-lg border border-[#6272a4] hover:border-[#8be9fd] transition-colors group">
        <div class="bg-[#8be9fd] text-[#282a36] font-bold w-8 h-8 rounded flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform">1</div>
        <div class="text-sm text-[#f8f8f2]">
          Haz <b>Login</b> en Postman<br>
          <code class="text-xs bg-[#282a36] px-1 rounded text-[#8be9fd]">POST /auth/login</code>
        </div>
      </div>
      <div class="flex items-center gap-4 bg-[#44475a] p-3 rounded-lg border border-[#6272a4] hover:border-[#8be9fd] transition-colors group">
        <div class="bg-[#8be9fd] text-[#282a36] font-bold w-8 h-8 rounded flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform">2</div>
        <div class="text-sm text-[#f8f8f2]">
          Copia el <b>Token</b> generado
          <span class="text-xs opacity-50 block">(Control+C / Command+C)</span>
        </div>
      </div>
      <a href="https://jwt.io" target="_blank" class="flex items-center gap-4 bg-[#ff79c6]/20 p-3 rounded-lg border border-[#ff79c6]/50 hover:bg-[#ff79c6]/40 transition-colors cursor-pointer group no-underline">
        <div class="bg-[#ff79c6] text-[#282a36] font-bold w-8 h-8 rounded flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform">3</div>
        <div class="text-sm font-bold text-[#ff79c6] group-hover:text-white">
          Ir a JWT.IO y PEGAR 🚀
        </div>
      </a>
    </div>
    <div class="bg-[#282a36] p-5 rounded-xl border border-[#50fa7b]/50 font-mono text-xs relative shadow-inner">
      <div class="absolute -top-3 right-4 bg-[#50fa7b] text-[#282a36] px-2 py-0.5 rounded text-[10px] font-bold uppercase tracking-widest">Target Data</div>
      <div class="space-y-4 mt-2">
        <div class="flex justify-between border-b border-[#6272a4] pb-2">
          <span class="text-[#bd93f9]">"sub"</span>
          <span class="text-[#f1fa8c] text-right">"tu@email.com"</span>
        </div>
        <div class="flex justify-between border-b border-[#6272a4] pb-2">
          <span class="text-[#bd93f9]">"role"</span>
          <span class="text-[#50fa7b] text-right">"USER"</span>
        </div>
        <div class="flex justify-between border-b border-[#6272a4] pb-2">
          <span class="text-[#bd93f9]">"exp"</span>
          <span class="text-[#8be9fd] text-right">1740... (Futuro)</span>
        </div>
        <div class="bg-[#ff5555]/20 text-[#ff5555] p-2 rounded text-center text-[10px] border border-[#ff5555]/50 mt-4">
          🔓 RECUERDA: ¡El token NO oculta los datos!<br>Cualquiera puede leer esto.
        </div>
      </div>
    </div>
  </div>
</div>

---

# 🗺️ Estado del Proyecto

<div class="flex justify-between items-center mt-12 relative">
  <!-- Línea de progreso -->
  <div class="absolute top-1/2 left-0 w-full h-1 bg-gray-700 -z-10"></div>
  
  <!-- Fases -->
  <div class="flex flex-col items-center">
    <div class="w-10 h-10 bg-green-500 rounded-full flex items-center justify-center font-bold text-black border-4 border-gray-900">1</div>
    <span class="text-xs mt-2 text-green-400">Base</span>
  </div>
  
  <div class="flex flex-col items-center">
    <div class="w-10 h-10 bg-green-500 rounded-full flex items-center justify-center font-bold text-black border-4 border-gray-900">2</div>
    <span class="text-xs mt-2 text-green-400">BCrypt</span>
  </div>

  <div class="flex flex-col items-center">
    <div class="w-10 h-10 bg-green-500 rounded-full flex items-center justify-center font-bold text-black border-4 border-gray-900">3</div>
    <span class="text-xs mt-2 text-green-400">JWT</span>
  </div>

  <div class="flex flex-col items-center">
    <div class="w-12 h-12 bg-blue-600 rounded-full flex items-center justify-center font-bold text-white border-4 border-white shadow-lg shadow-blue-500/50 animate-pulse">4</div>
    <span class="text-sm mt-2 font-bold text-blue-400">FILTRO</span>
  </div>

  <div class="flex flex-col items-center opacity-50">
    <div class="w-8 h-8 bg-gray-700 rounded-full flex items-center justify-center font-bold text-gray-400 border-4 border-gray-900">5</div>
    <span class="text-xs mt-2">Roles</span>
  </div>
  
  <div class="flex flex-col items-center opacity-50">
    <div class="w-8 h-8 bg-gray-700 rounded-full flex items-center justify-center font-bold text-gray-400 border-4 border-gray-900">6</div>
    <span class="text-xs mt-2">Final</span>
  </div>
</div>

---
layout: section
---

# 🛑 FASE 4
## El Traductor (UserDetailsService)

---

# 4.1 El Problema: Spring no te entiende 🤷‍♂️
## ¿Quién es "Usuario"?

Spring Security es un framework genérico. No sabe que tu clase se llama `Usuario`, ni que tiene un campo `email`, ni cómo guardas los roles.

Spring solo entiende un lenguaje universal: **`UserDetails`**.

<div class="grid grid-cols-2 gap-8 mt-8 items-center">
<div class="bg-red-900/20 p-4 rounded border border-red-500/50 flex flex-col items-center h-full justify-center">
<div class="text-4xl mb-4">🆔</div>
<h3 class="font-bold text-red-300">Tu Clase "Usuario"</h3>
<p class="text-xs opacity-70 mt-2 text-center">Tiene tus datos personalizados (dni, dirección, avatar...).<br>Spring no sabe ni qué es.</p>
</div>

<div class="bg-green-900/20 p-4 rounded border border-green-500/50 flex flex-col items-center h-full justify-center relative">
<div class="absolute -left-4 top-1/2 -translate-y-1/2 bg-gray-800 rounded-full p-1 border border-gray-600 z-10">➡️</div>
<div class="text-4xl mb-4">👮‍♂️</div>
<h3 class="font-bold text-green-300">UserDetails (Spring)</h3>
<p class="text-xs opacity-70 mt-2 text-center">Es una ficha estándar con solo lo que le importa a seguridad: username, password y authorities.</p>
</div>
</div>

---

# 4.2 La Estrategia (Receta) 📝
## Cómo crear el Traductor paso a paso

Antes de escribir código, tracemos el plan para conectar Spring Security con nuestra base de datos.
Utilizaremos una clase personalizada en el paquete `security`.

<div class="mt-2 flex flex-col gap-2 font-mono">

<div class="flex items-center gap-2 px-3 py-2 bg-gray-900/50 rounded-lg border-l-4 border-blue-500 hover:bg-gray-800 transition-colors">
<div class="h-6 w-6 flex items-center justify-center rounded-full bg-blue-600 text-[10px] font-bold shadow-lg shadow-blue-500/50">1</div>
<div class="flex flex-col">
<span class="text-xs font-bold text-blue-200">Clase security</span>
<span class="text-[9px] text-gray-400">Ej: <code>CineUserDetailsService</code></span>
</div>
</div>

<div class="flex items-center gap-2 px-3 py-2 bg-gray-900/50 rounded-lg border-l-4 border-purple-500 hover:bg-gray-800 transition-colors">
<div class="h-6 w-6 flex items-center justify-center rounded-full bg-purple-600 text-[10px] font-bold shadow-lg shadow-purple-500/50">2</div>
<div class="flex flex-col">
<span class="text-xs font-bold text-purple-200">UserDetailsService ✨</span>
<span class="text-[9px] text-gray-400">Intefaz mágica de Spring</span>
</div>
</div>

<div class="flex items-center gap-2 px-3 py-2 bg-gray-900/50 rounded-lg border-l-4 border-yellow-500 hover:bg-gray-800 transition-colors">
<div class="h-6 w-6 flex items-center justify-center rounded-full bg-yellow-600 text-[10px] font-bold shadow-lg shadow-yellow-500/50">3</div>
<div class="flex flex-col">
<span class="text-xs font-bold text-yellow-200">Repo 💉</span>
<span class="text-[9px] text-gray-400"><code>UsuarioRepository</code> en BD</span>
</div>
</div>

<div class="flex items-center gap-2 px-3 py-2 bg-gray-900/50 rounded-lg border-l-4 border-green-500 hover:bg-gray-800 transition-colors">
<div class="h-6 w-6 flex items-center justify-center rounded-full bg-green-600 text-[10px] font-bold shadow-lg shadow-green-500/50">4</div>
<div class="flex flex-col">
<span class="text-xs font-bold text-green-200">loadUserByUsername</span>
<span class="text-[9px] text-gray-400">Traduce: <code>Usuario</code> -> <code>UserDetails</code></span>
</div>
</div>

</div>

---

# 4.3 CineUserDetailsService 🗣️

Necesitamos una clase que haga de puente. Spring la llamará cada vez que alguien intente entrar.
<br>📂 `src/main/java/.../security/CineUserDetailsService.java`

<div class="h-[400px] overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-purple-500 scrollbar-track-gray-800">

```java
@Service
@RequiredArgsConstructor
public class CineUserDetailsService implements UserDetailsService {

    private final UsuarioRepository repo; 

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // 1. Buscamos el usuario en NUESTRA base de datos
        Usuario u = repo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // 2. LO TRADUCIMOS al formato que Spring Security entiende
        // User es una implementación de UserDetails que nos regala Spring
        return User.builder()
            .username(u.getEmail())
            .password(u.getPassword()) // La contraseña cifrada
            .roles(u.getRol())         // Asigna los permisos
            .build();
    }
}
```
</div>

<div class="mt-4 bg-blue-900/30 border border-blue-500/50 p-2 rounded text-xs text-center">
    Gracias a este servicio, Spring ya puede validar si el usuario existe y qué permisos tiene.
</div>

---

# 🗣️ FASE 5
<div class="flex flex-col items-center justify-center h-full w-full text-center">
  <h2 class="text-4xl font-bold mb-4">El Filtro (JwtAuthenticationFilter)</h2>
  <div class="text-xl opacity-70">Interceptando Peticiones</div>
</div>

---

# 5.1 ¿Qué es un Filtro? 🛡️
## El Portero de la Discoteca

Antes de que **NADIE** llegue a tu Controlador (Zona VIP), tiene que pasar por el Filtro.

<div class="flex items-center justify-between mt-4 px-4 relative">

<!-- CLIENTE -->
<div class="flex flex-col items-center gap-2 z-10 w-24">
<div class="text-4xl">🕵️</div>
<div class="bg-gray-700 px-2 py-1 rounded text-[10px] w-full text-center">Cliente</div>
<div class="text-[8px] opacity-60">"GET /peliculas"</div>
</div>

<!-- FLECHA 1 -->
<div class="flex-1 h-0.5 bg-gray-600 mx-1 relative">
<div class="absolute -top-3 left-1/2 -translate-x-1/2 text-[10px] opacity-50">Request</div>
<div class="absolute -right-1 -top-1.5 text-gray-600 text-xs">▶</div>
</div>

<!-- FILTRO (PORTERO) -->
<div class="flex flex-col items-center gap-1 z-10 w-40">
<div class="absolute -top-6 text-2xl animate-bounce">🛑</div>
<div class="bg-blue-600 p-2 rounded-lg border-2 border-blue-400 shadow-[0_0_20px_rgba(59,130,246,0.3)] text-center w-full">
<h3 class="font-bold text-white text-xs">JwtFilter</h3>
<p class="text-[8px] text-blue-100 leading-tight mt-0.5">¿Token válido?</p>
</div>

<!-- CAMINO DE RECHAZO -->
<div class="absolute -bottom-10 flex flex-col items-center opacity-0 animate-pulse delay-1000">
<div class="h-4 w-0.5 bg-red-500/50"></div>
<div class="bg-red-900/80 px-2 py-0.5 rounded text-[8px] text-red-200 border border-red-500">403</div>
</div>
</div>

<!-- FLECHA 2 -->
<div class="flex-1 h-0.5 bg-green-500/50 mx-1 relative">
<div class="absolute -top-3 left-1/2 -translate-x-1/2 text-[8px] text-green-400 whitespace-nowrap">SecurityContext</div>
<div class="absolute -right-1 -top-1.5 text-green-500/50 text-xs">▶</div>
</div>

<!-- CONTROLADOR (VIP) -->
<div class="flex flex-col items-center gap-2 z-10 w-24">
<div class="text-4xl">🕺</div>
<div class="bg-green-600 px-2 py-1 rounded-lg border-2 border-green-400 shadow-lg w-full text-center">
<div class="font-bold text-[10px]">Controller</div>
<div class="text-[8px] opacity-80">VIP</div>
</div>
</div>

</div>

<div class="mt-8 grid grid-cols-2 gap-4">
<div class="bg-gray-800 p-2 rounded border-l-2 border-blue-500 text-[10px]">
<strong class="text-blue-400">1. Intercepta:</strong><br>
El filtro se pone en medio de TODAS las peticiones.
</div>
<div class="bg-gray-800 p-2 rounded border-l-2 border-green-500 text-[10px]">
<strong class="text-green-400">2. Autentica:</strong><br>
Si el token vales, inyecta el `Authentication` y deja pasar.
</div>
</div>

---

# 5.2 Eligiendo la Herramienta 🛠️
## ¿Por qué `OncePerRequestFilter`?

Spring tiene muchos tipos de filtros. Podríamos usar `Filter` estándar de Java, pero...

<div class="grid grid-cols-2 gap-8 mt-8 items-center">
    <div class="bg-red-900/20 p-6 rounded-xl border border-red-500/30 opacity-70">
        <h3 class="text-red-400 font-bold mb-2">Filtro Estándar</h3>
        <p class="text-sm">Puede ejecutarse múltiples veces por petición (ej: en forwards o includes). ¡Peligro de validar el token 2 veces!</p>
    </div>
    <div class="bg-green-900/20 p-6 rounded-xl border-2 border-green-500 shadow-[0_0_30px_rgba(34,197,94,0.2)]">
        <h3 class="text-green-400 font-bold mb-2">OncePerRequestFilter</h3>
        <p class="text-sm">Spring nos garantiza que se ejecuta <b>EXACTAMENTE UNA VEZ</b> por petición HTTP. Eficiencia y seguridad.</p>
    </div>
</div>

---

# 5.3 Teoría del Filtro 🧠
## El Ciclo de Vida

<div class="grid grid-cols-2 gap-4 mt-8">
<div class="bg-gray-800 p-4 rounded-xl border border-gray-700">
    <h3 class="text-blue-400 font-bold text-sm mb-2">Ciclo de Vida 🔄</h3>
    <ul class="text-[10px] space-y-2 opacity-80 list-disc ml-4">
        <li>El usuario lanza una petición HTTP.</li>
        <li>Spring intercepta la petición ANTES de llamar al Controller.</li>
        <li>Ejecuta nuestro método <code>doFilterInternal</code>.</li>
        <li>Si todo va bien, pasamos la petición al siguiente eslabón (`filterChain`).</li>
    </ul>
</div>
<div class="bg-gray-800 p-4 rounded-xl border border-gray-700">
    <h3 class="text-purple-400 font-bold text-sm mb-2">Responsabilidades 👮‍♂️</h3>
    <ul class="text-[10px] space-y-2 opacity-80 list-disc ml-4">
        <li>Mirar si trae "DNI" (Token en Header).</li>
        <li>Validar que es auténtico (Firma y Expiración).</li>
        <li>Buscar al dueño en la lista de invitados (`UserDetailsService`).</li>
        <li>Darle el sello VIP (`SecurityContext`).</li>
    </ul>
</div>
</div>

---

# 5.4 Pensando la Lógica 🧠
## ¿Qué tiene que hacer el filtro?

Antes de ver la receta, visualicemos el flujo de datos.

<div class="flex justify-center mt-12">
    <div class="flex items-center gap-2">
        <div class="flex flex-col items-center">
            <span class="text-2xl">👂</span>
            <span class="text-xs bg-gray-700 px-2 py-1 rounded mt-1">Escuchar</span>
            <span class="text-[8px] opacity-60">Header Auth</span>
        </div>
        <div class="h-[2px] w-12 bg-gray-600"></div>
        <div class="flex flex-col items-center">
            <span class="text-2xl">✂️</span>
            <span class="text-xs bg-gray-700 px-2 py-1 rounded mt-1">Extraer</span>
            <span class="text-[8px] opacity-60">"Bearer " fuera</span>
        </div>
        <div class="h-[2px] w-12 bg-gray-600"></div>
        <div class="flex flex-col items-center">
            <span class="text-2xl">🔍</span>
            <span class="text-xs bg-gray-700 px-2 py-1 rounded mt-1">Validar</span>
            <span class="text-[8px] opacity-60">Firma OK?</span>
        </div>
        <div class="h-[2px] w-12 bg-gray-600"></div>
        <div class="flex flex-col items-center">
            <span class="text-2xl">🎟️</span>
            <span class="text-xs bg-gray-700 px-2 py-1 rounded mt-1">Sellar</span>
            <span class="text-[8px] opacity-60">SecurityContext</span>
        </div>
    </div>
</div>

---

# 5.5 Profundizando: El Header y el Token 🕵️‍♂️
## Paso 1: Identificación y Validación

<div class="grid grid-cols-2 gap-6 mt-4 text-xs">

<div class="bg-gray-800 p-4 rounded-lg border border-blue-500/30">

<h3 class="text-blue-400 font-bold mb-2">1. Escuchar y Extraer 👂</h3>

- **HttpServletRequest**: El objeto Java que representa la petición HTTP completa.
- **Header "Authorization"**: Buscamos esto. Debe tener el formato `Bearer eyJhbGci...`.
- **substring(7)**: "Bearer " son 7 caracteres. Los quitamos para obtener el token puro.
- **¿Y si no está?**: Si es null o no empieza por "Bearer", no lanzamos error. Hacemos `filterChain.doFilter` para que pase como **ANÓNIMO**. Spring decidirá luego si entra o no.

</div>

<div class="bg-gray-800 p-4 rounded-lg border border-purple-500/30">

<h3 class="text-purple-400 font-bold mb-2">2. Validar con JwtUtil 🔍</h3>

- **Validar**: Llamamos a `jwtUtil.validateToken()`.
- **Integridad**: Recalcula el hash con nuestra `SECRET_KEY`. Si han tocado una letra, el hash no coincide -> **FALSO**.
- **Expiración**: Mira la fecha `exp` del payload. Si hoy > exp -> **CADUCADO**.
- Solo si todo es OK, pasamos al siguiente paso.

</div>

</div>

---

# 5.6 Profundizando: El Sello (SecurityContext) 🛂
## Paso 2: Autenticación Efectiva

<div class="grid grid-cols-2 gap-6 mt-4 text-xs">

<div class="bg-gray-800 p-4 rounded-lg border border-green-500/30">

<h3 class="text-green-400 font-bold mb-2">3. El Sello Oficial (Authentication) 🎟️</h3>

- Spring no entiende de JWTs, entiende de objetos `Authentication`.
- **UsernamePasswordAuthenticationToken**: Es la implementación estándar.
  <br>`new ...(usuario, null, roles)`
- **Usuario**: El `UserDetails` completo que cargamos de la BD.
- **Null**: No guardamos la password en memoria por seguridad.

</div>

<div class="bg-gray-800 p-4 rounded-lg border border-yellow-500/30">

<h3 class="text-yellow-400 font-bold mb-2">4. Contexto y Cadena ⛓️</h3>

- **SecurityContextHolder**: Es una "caja fuerte" global (ThreadLocal). Si pones ahí el token, Spring asume que estás LOGUEADO.
- **¡CRUCIAL!**: Al final, SIEMPRE llamar a `filterChain.doFilter(request, response)`.
- Si se te olvida, la petición muere ahí y se queda la pantalla en blanco. Es pasar el testigo al siguiente (o al Controller).

</div>

</div>

---

# 5.7 La Estrategia (Receta) 📝
## Checklist de Implementación

<div class="mt-2 flex flex-col gap-2 font-mono">
<div class="flex items-center gap-2 px-3 py-2 bg-gray-900/50 rounded-lg border-l-4 border-blue-500 hover:bg-gray-800 transition-colors">
<div class="h-6 w-6 flex items-center justify-center rounded-full bg-blue-600 text-[10px] font-bold shadow-lg shadow-blue-500/50">1</div>
<div class="flex flex-col">
<span class="text-xs font-bold text-blue-200">Clase security</span>
<span class="text-[9px] text-gray-400">Extiende de <code>OncePerRequestFilter</code></span>
</div>
</div>
<div class="flex items-center gap-2 px-3 py-2 bg-gray-900/50 rounded-lg border-l-4 border-purple-500 hover:bg-gray-800 transition-colors">
<div class="h-6 w-6 flex items-center justify-center rounded-full bg-purple-600 text-[10px] font-bold shadow-lg shadow-purple-500/50">2</div>
<div class="flex flex-col">
<span class="text-xs font-bold text-purple-200">Inyectar Dependencias 💉</span>
<span class="text-[9px] text-gray-400"><code>JwtUtil</code> (validador) + <code>UserDetailsService</code> (traductor)</span>
</div>
</div>
<div class="flex items-center gap-2 px-3 py-2 bg-gray-900/50 rounded-lg border-l-4 border-yellow-500 hover:bg-gray-800 transition-colors">
<div class="h-6 w-6 flex items-center justify-center rounded-full bg-yellow-600 text-[10px] font-bold shadow-lg shadow-yellow-500/50">3</div>
<div class="flex flex-col">
<span class="text-xs font-bold text-yellow-200">Lógica Principal 🧠</span>
<span class="text-[9px] text-gray-400">Override <code>doFilterInternal</code></span>
</div>
</div>
</div>

---

# 5.8 El Flujo de Autenticación 🚦
## ¿Qué significa realmente "Estar Autenticado"?

<div class="grid grid-cols-2 gap-8 mt-8">

<!-- CAMINO A: ANÓNIMO -->
<div class="bg-gray-800/50 p-6 rounded-xl border border-gray-600 relative overflow-hidden group">
    <div class="absolute top-0 right-0 p-2 opacity-10 text-6xl group-hover:opacity-20 transition-opacity">👻</div>
    <h3 class="text-gray-400 font-bold mb-4 text-lg">Camino A: Anónimo</h3>
    <p class="text-xs mb-4">Si no hay token o es inválido:</p>
    <div class="bg-gray-900 p-3 rounded font-mono text-xs border-l-4 border-gray-500">
        filterChain.doFilter(req, res);
    </div>
    <ul class="text-[10px] mt-4 space-y-2 opacity-70 list-disc ml-4">
        <li>La petición sigue su curso.</li>
        <li>Spring Security ve el `SecurityContext` <b>VACÍO</b>.</li>
        <li>Si intenta entrar a una zona protegida -> <b>403 Forbidden</b>.</li>
    </ul>
</div>

<!-- CAMINO B: AUTENTICADO -->
<div class="bg-green-900/20 p-6 rounded-xl border-2 border-green-500 relative overflow-hidden group shadow-[0_0_20px_rgba(34,197,94,0.1)]">
    <div class="absolute top-0 right-0 p-2 opacity-10 text-6xl group-hover:opacity-20 transition-opacity text-green-500">🎟️</div>
    <h3 class="text-green-400 font-bold mb-4 text-lg">Camino B: Autenticado</h3>
    <p class="text-xs mb-4">Si el token es válido:</p>
    <ol class="text-[10px] space-y-3 list-decimal ml-4">
        <li><b>Extracción</b>: Sacamos el username del token.</li>
        <li><b>Carga</b>: <code>UserDetailsService</code> busca los datos reales en BD.</li>
        <li><b>Creación del DNI</b>: Instanciamos <code>UsernamePasswordAuthenticationToken</code>.</li>
        <li><b>Sellado</b>: Lo metemos en la caja fuerte global.</li>
    </ol>
    <div class="bg-gray-900 p-3 rounded font-mono text-xs border-l-4 border-green-500 mt-4">
        SecurityContextHolder...setAuthentication(auth);<br>
        filterChain.doFilter(req, res);
    </div>
</div>

</div>

<div class="mt-8 text-center bg-blue-900/30 p-3 rounded border border-blue-500/30">
    <span class="text-blue-300 font-bold text-sm">💡 Concepto Clave:</span>
    <span class="text-xs text-blue-100 ml-2">Para Spring, "Estar Autenticado" solo significa <b>que haya un objeto Authentication válido dentro del SecurityContext</b>. No le importa de dónde vino (Token, Session, Basic Auth...).</span>
</div>

---

# 5.9 Anatomía de `doFilterInternal` 🩻
## Entendiendo los parámetros antes de programar

```java
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
```

<div class="grid grid-cols-3 gap-4 mt-6">
    <div class="flex flex-col items-center bg-[#282a36] p-3 rounded-lg border border-blue-500/30">
        <div class="text-2xl mb-2">📩</div>
        <h3 class="font-bold text-blue-300 text-xs">request</h3>
        <p class="text-[9px] text-center opacity-70 mt-1">La carta que llega.<br>Trae el <b>Header Authorization</b>.</p>
    </div>
    <div class="flex flex-col items-center bg-[#282a36] p-3 rounded-lg border border-purple-500/30">
        <div class="text-2xl mb-2">📤</div>
        <h3 class="font-bold text-purple-300 text-xs">response</h3>
        <p class="text-[9px] text-center opacity-70 mt-1">El buzón de salida.<br>Si el token está mal, aquí escribimos el error.</p>
    </div>
    <div class="flex flex-col items-center bg-[#282a36] p-3 rounded-lg border border-yellow-500/30">
        <div class="text-2xl mb-2">⛓️</div>
        <h3 class="font-bold text-yellow-300 text-xs">filterChain</h3>
        <p class="text-[9px] text-center opacity-70 mt-1">El siguiente paso.<br><code>filterChain.doFilter()</code> deja pasar al usuario.</p>
    </div>
</div>

---

# 5.10 JwtAuthenticationFilter: El Código 💻

<div class="h-[400px] overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-purple-500 scrollbar-track-gray-800 text-xs">

```java
// IMPORTS NECESARIOS (Copia y pega con confianza 🚀)
import com.iesantoniogala.cine.service.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;          // 1. Fábrica de Tokens
    private final UserDetailsService userDetailsService; // 2. Traductor de Usuarios

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // A. OBTENER EL HEADER
        String authHeader = request.getHeader("Authorization");

        // B. COMPROBAR SI VIENE EL TOKEN (¿Trae zapatillas?)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Pasa, pero sin autenticar (invitado)
            return;
        }

        // C. EXTRAER EL TOKEN (Quitar la palabra "Bearer ")
        String token = authHeader.substring(7);

        // D. VALIDAR TOKEN Y USUARIO
        // 1. Validamos firma y expiración
        // 2. Comprobamos que no esté ya autenticado en el contexto actual
        if (jwtUtil.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            String username = jwtUtil.extractUsername(token);

            // E. CARGAR USUARIO DE LA BASE DE DATOS
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // F. CREAR OBJETO DE AUTENTICACIÓN (El DNI Oficial de Spring)
            // Este objeto es el que Spring "entiende" y guarda en la caja fuerte.
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );

            // G. GUARDAR EN EL CONTEXTO DE SEGURIDAD (Spring ya sabe quién eres)
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // H. PASAR AL SIGUIENTE FILTRO (O al Controller)
        filterChain.doFilter(request, response);
    }
}
```
</div>


---
layout: section
---

# 🛡️ FASE 6
## Configuración Final (El Contrato)

Hemos creado el "Portero" (`JwtAuthenticationFilter`), **pero nadie le ha dicho dónde ponerse**.
Por defecto, Spring Security NO lo usa.

<div class="mt-4 mb-4 bg-yellow-900/30 border border-yellow-500/50 p-4 rounded text-center">
    <h3 class="font-bold text-yellow-300">🔗 Hay que registrarlo en la cadena</h3>
    <p class="text-sm opacity-80">"Pon este filtro ANTES del filtro de usuario/contraseña estándar".</p>
</div>

---

# 6.1 SecurityConfig: La Conexión 🔌 (Concepto)

Volvemos a nuestra clase de configuración para inyectar y añadir el filtro.
<br>📂 `src/main/java/.../security/SecurityConfig.java`

<div class="bg-blue-900/20 border border-blue-500/50 p-4 rounded mb-6 mt-4">
    <h3 class="font-bold text-blue-300 mb-2">🤔 ¿Qué es <code>UsernamePasswordAuthenticationFilter</code>?</h3>
    <div class="grid grid-cols-[auto_1fr] gap-4 items-center">
        <div class="text-4xl">⚓</div>
        <div>
            <p class="text-xs opacity-90 mb-2">
                Es el filtro <b>Clásico</b> de Spring. El que lee <code>username</code> y <code>password</code> de un formulario HTML y crea una sesión en memoria (JSESSIONID).
            </p>
            <p class="text-xs font-bold text-yellow-300">
                NO lo vamos a usar, lo usamos de ANCLA.
            </p>
        </div>
    </div>
    <div class="bg-gray-900/50 p-2 rounded text-[10px] font-mono border-l-4 border-blue-400 mt-2">
        addFilterBefore(miFiltro, ElFiltroClasico.class);
    </div>
    <p class="text-[10px] mt-2 opacity-70">
        Le decimos a Spring: <i>"No sé qué otros filtros hay, pero pon el mío (JWT) <b>JUSTO ANTES</b> de que intentes buscar un login de formulario estándar".</i>
    </p>
</div>

---

# 6.1b SecurityConfig: El Código 💻

<div class="h-[400px] overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-purple-500 scrollbar-track-gray-800 text-xs">

```java
// IMPORTS (Copia y pega 🚀)
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // <--- 1. Activa @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter; // <--- 2. Inyectamos nuestro filtro

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(c -> c.disable()) // API Rest pura, no cookies
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No guardar estado
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Login público
                .anyRequest().authenticated()            // Todo lo demás cerrado
            )
            // 3. AQUÍ LO REGISTRAMOS ⬇️
            // Ponemos nuestro filtro ANTES del filtro de login clásico (el ancla)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```
</div>

<div class="mt-4 text-xs opacity-70 border-t border-gray-700 pt-2">
    <b>Resultado:</b> Ahora, cada petición pasará primero por <code>jwtFilter</code>. Si lleva token válido, entra como usuario autenticado. Si no, sigue la cadena (y probablemente choque contra la pared de <code>.authenticated()</code>).
</div>

---

# 6.2 TestSecurityController

Vamos a probar si el portero funciona.

```java
@RestController
@RequestMapping("/api/test")
public class TestSecurityController {

    @GetMapping("/publico")
    public String publico() { 
        return "Pasar, pasar, está abierto."; 
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')") // <--- Solo VIPs
    public String soloAdmin() {
        return "Hola, jefe. Aquí tiene los datos.";
    }
}
```

---

# 6.3 La Prueba de Fuego 🔥

1.  Hacer petición a `/api/test/admin` SIN token.
    -   Resultado: `403 Forbidden` (El filtro no encontró token).
2.  Hacer Login como usuario normal (USER). Copiar token.
3.  Hacer petición a `/api/test/admin` CON token de USER.
    -   Resultado: `403 Forbidden` (El filtro te dejó pasar, pero `@PreAuthorize` vio que no eras ADMIN).
4.  Hacer Login como ADMIN. Copiar token.
5.  Hacer petición a `/api/test/admin`.
    -   Resultado: `200 OK`.

<div class="text-center font-bold text-green-400 mt-4 text-2xl">
¡SISTEMA SEGURO! 🚀
</div>

---
layout: section
---

# 🏛️ ARQUITECTURA FINAL

---

# Flujo Completo de una Petición

<div class="flex justify-center items-center mt-2">
<div class="space-y-2 w-full max-w-2xl relative">

<!-- Línea conectora central -->
<div class="absolute left-1/2 top-4 bottom-4 w-1 bg-[#6272a4] -ml-0.5 z-0 rounded"></div>

<!-- 1. Cliente -->
<div class="relative z-10 flex justify-center">
<div class="bg-[#bd93f9] text-[#282a36] font-bold px-6 py-2 rounded-full shadow-lg border-2 border-[#f8f8f2] flex items-center gap-2 text-sm">
👤 Cliente <span class="text-[10px] bg-black/20 px-2 py-0.5 rounded text-white">GET /api/test/admin</span>
</div>
</div>

<!-- Flecha abajo -->
<div class="relative z-10 flex justify-center text-[#ff79c6] text-sm">⬇️</div>

<!-- 2. Filtro JWT -->
<div class="relative z-10 grid grid-cols-2 gap-4 items-center">
<div class="text-right pr-4">
<div class="text-[#ff79c6] font-bold text-sm">1. Filtro JWT</div>
<div class="text-[10px] text-[#f8f8f2] opacity-70">Intercepta "Bearer Token"</div>
</div>
<div class="bg-[#282a36] border-2 border-[#ff79c6] p-2 rounded-lg shadow-lg text-xs">
🔎 ¿Token Válido?
</div>
</div>

<!-- 3. UserDetailsService -->
<div class="relative z-10 grid grid-cols-2 gap-4 items-center">
<div class="bg-[#282a36] border-2 border-[#8be9fd] p-2 rounded-lg shadow-lg text-right text-xs">
📜 Carga Usuario + Roles
</div>
<div class="text-left pl-4">
<div class="text-[#8be9fd] font-bold text-sm">2. UserDetailsService</div>
<div class="text-[10px] text-[#f8f8f2] opacity-70">Consulta en Base de Datos</div>
</div>
</div>

<!-- 4. SecurityContext -->
<div class="relative z-10 flex justify-center py-1">
<div class="bg-[#44475a] text-[#f8f8f2] px-3 py-1 rounded border border-[#6272a4] text-[10px]">
💾 SecurityContextHolder (Usuario Autenticado)
</div>
</div>

<!-- 5. Autorización -->
<div class="relative z-10 grid grid-cols-2 gap-4 items-center">
<div class="text-right pr-4">
<div class="text-[#ff5555] font-bold text-sm">3. @PreAuthorize</div>
<div class="text-[10px] text-[#f8f8f2] opacity-70">¿Tienes Rol 'ADMIN'?</div>
</div>
<div class="bg-[#282a36] border-2 border-[#ff5555] p-2 rounded-lg shadow-lg flex items-center gap-2 text-xs">
<div class="bg-red-500 w-2 h-2 rounded-full animate-pulse"></div>
<span>Guardia Final</span>
</div>
</div>

<!-- Flecha abajo -->
<div class="relative z-10 flex justify-center text-[#50fa7b] text-sm">⬇️</div>

<!-- 6. Controlador -->
<div class="relative z-10 flex justify-center">
<div class="bg-[#50fa7b] text-[#282a36] font-bold px-6 py-2 rounded-lg shadow-xl border-2 border-[#f8f8f2] flex flex-col items-center">
<span class="text-sm">🏛️ Controlador</span>
<span class="text-[10px] font-mono mt-1">return "200 OK";</span>
</div>
</div>

</div>
</div>

---

# 🏁 Conclusión

La seguridad no es magia. Es una cadena de responsabilidades.

1.  **BCrypt** protege la base de datos.
2.  **JWT** protege la comunicación.
3.  **Filtro** identifica al usuario.
4.  **Roles** limitan el acceso.

Si una sola de estas piezas falla, **el sistema entero es vulnerable**.

---

# 🎓 Práctica P2: Proyecto Cine con JWT

Implementad la seguridad completa en **Cine V2** con los siguientes requisitos:

<div class="grid grid-cols-2 gap-4 text-xs mt-2">

<div>

<h3 class="text-blue-400 font-bold mb-1">1. Token Enriquecido 🎟️</h3>
<ul class="list-disc ml-4 opacity-80">
<li><b>Payload:</b> Email, Rol, ID Usuario, Emisión, Expiración.</li>
</ul>

<h3 class="text-green-400 font-bold mb-1 mt-2">2. Endpoint Personales 👤</h3>
<ul class="list-disc ml-4 opacity-80">
<li>`GET /api/me`: Devuelve info del usuario autenticado.</li>
<li><b>Prohibido:</b> Recibir ID por parámetro.</li>
</ul>

<h3 class="text-yellow-400 font-bold mb-1 mt-2">3. Sistema de Roles 🛡️</h3>
<ul class="list-disc ml-4 opacity-80">
<li><b>Consultar Pelis:</b> USER y ADMIN.</li>
<li><b>Crear/Editar/Borrar:</b> Solo ADMIN.</li>
<li><b>Error:</b> Devolver <code>403 Forbidden</code> si falla.</li>
</ul>

</div>

<div>

<h3 class="text-red-400 font-bold mb-1">4. Casos de Prueba (Demo) 🧪</h3>
<div class="bg-gray-900/50 p-2 rounded border border-gray-700">
<ul class="list-none space-y-1">
<li>❌ <b>Petición sin token</b>: 403/401.</li>
<li>❌ <b>Token manipulado</b>: Firma inválida.</li>
<li>❌ <b>Token expirado</b>: Fecha pasada.</li>
<li>❌ <b>USER -> Zona ADMIN</b>: Acceso denegado.</li>
<li>✅ <b>ADMIN -> Zona ADMIN</b>: Acceso permitido.</li>
</ul>
</div>

</div>

</div>
