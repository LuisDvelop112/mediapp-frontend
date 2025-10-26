# 🧪 Guía de Pruebas Unitarias - MediApp

## 📋 Descripción General

Este proyecto incluye pruebas unitarias completas usando **JUnit 5**, **Mockito** y **Spring Boot Test** para garantizar la calidad y funcionalidad del código.

## 🏗️ Estructura de Pruebas

```
src/test/java/com/mediapp/citasbackend/
├── services/
│   ├── AuthServiceImplTest.java        # Pruebas del servicio de autenticación
│   └── UsuarioServiceImplTest.java     # Pruebas del servicio de usuarios
├── repositories/
│   └── UsuarioRepositoryTest.java      # Pruebas del repositorio de usuarios
├── controllers/
│   └── AuthControllerTest.java         # Pruebas del controlador de autenticación
├── security/
│   └── JwtServiceTest.java             # Pruebas del servicio JWT
└── CitasMedicasApplicationTests.java   # Prueba de contexto de Spring
```

## 🔧 Tecnologías Utilizadas

- **JUnit 5** (Jupiter): Framework de pruebas
- **Mockito**: Framework para mocking
- **Spring Boot Test**: Soporte para pruebas de integración
- **MockMvc**: Pruebas de endpoints REST
- **DataJpaTest**: Pruebas de repositorios JPA
- **AssertJ**: Assertions fluidas (opcional)

## 📊 Cobertura de Pruebas

### 1. AuthServiceImplTest
✅ **9 pruebas** que cubren:
- Registro exitoso de usuarios
- Validación de duplicados (email)
- Login con credenciales válidas
- Manejo de credenciales inválidas
- Validación de estados de cuenta (activo/inactivo/bloqueado)
- Generación y validación de refresh tokens

### 2. UsuarioServiceImplTest
✅ **15 pruebas** que cubren:
- CRUD completo de usuarios
- Validación de datos (email, teléfono, nombre)
- Manejo de duplicados
- Búsqueda por diferentes criterios
- Conteo de usuarios
- Manejo de errores (ResourceNotFoundException, InvalidDataException)

### 3. UsuarioRepositoryTest
✅ **10 pruebas** que cubren:
- Persistencia de datos
- Queries personalizadas
- Búsqueda por email, tipo, estado, ciudad
- Operaciones de conteo
- Búsqueda por nombre o apellido

### 4. AuthControllerTest
✅ **12 pruebas** que cubren:
- Endpoints de registro (/api/auth/register)
- Endpoints de login (/api/auth/login)
- Endpoints de refresh token (/api/auth/refresh)
- Validación de request bodies
- Manejo de errores HTTP (400, 401, 409)
- Validación de headers

### 5. JwtServiceTest
✅ **10 pruebas** que cubren:
- Generación de tokens de acceso y refresh
- Extracción de información del token
- Validación de tokens
- Manejo de tokens malformados y expirados
- Verificación de unicidad de tokens

## 🚀 Ejecutar las Pruebas

### Opción 1: Maven (Recomendado)

```bash
# Ejecutar todas las pruebas
./mvnw test

# Ejecutar con información detallada
./mvnw test -Dtest.verbose=true

# Ejecutar solo una clase de prueba específica
./mvnw test -Dtest=AuthServiceImplTest

# Ejecutar con reporte de cobertura
./mvnw test jacoco:report
```

### Opción 2: IDE (IntelliJ IDEA / Eclipse)

1. **Ejecutar todas las pruebas:**
   - Clic derecho en `src/test/java` → "Run All Tests"

2. **Ejecutar una clase específica:**
   - Clic derecho en la clase de prueba → "Run 'NombreTest'"

3. **Ejecutar un método específico:**
   - Clic derecho en el método → "Run 'nombreMetodo()'"

### Opción 3: Gradle (si aplica)

```bash
./gradlew test
```

## 📈 Ver Reportes de Pruebas

Después de ejecutar las pruebas, los reportes se generan en:

```
target/
├── surefire-reports/       # Reportes XML/TXT de Maven
└── site/
    └── jacoco/            # Reporte de cobertura (si usas JaCoCo)
        └── index.html
```

### Agregar JaCoCo para Cobertura de Código

Agrega esto a tu `pom.xml`:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## 🎯 Buenas Prácticas Implementadas

### 1. **Nomenclatura Clara**
```java
@Test
@DisplayName("Registro exitoso de nuevo usuario")
void testRegister_Success() { ... }
```

### 2. **Patrón AAA (Arrange-Act-Assert)**
```java
// Arrange: Preparar datos
when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

// Act: Ejecutar acción
AuthResponseDTO response = authService.register(request);

// Assert: Verificar resultado
assertNotNull(response);
assertEquals("expectedValue", response.getValue());
```

### 3. **Uso de Mocks**
```java
@Mock
private UsuarioRepository usuarioRepository;

@InjectMocks
private UsuarioServiceImpl usuarioService;
```

### 4. **Pruebas de Casos Límite**
- ✅ Datos válidos
- ✅ Datos inválidos
- ✅ Valores nulos
- ✅ Valores duplicados
- ✅ Recursos no encontrados

### 5. **Pruebas de Excepciones**
```java
assertThrows(ResourceNotFoundException.class, 
    () -> usuarioService.buscarPorId(999));
```

## 🔍 Ejemplos de Pruebas

### Prueba de Servicio con Mockito

```java
@Test
void testGuardarUsuario_Success() {
    // Arrange
    when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

    // Act
    Usuario result = usuarioService.guardarUsuario(usuario);

    // Assert
    assertNotNull(result);
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
}
```

### Prueba de Repositorio con DataJpaTest

```java
@DataJpaTest
class UsuarioRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void testFindByEmail() {
        Usuario saved = entityManager.persistAndFlush(usuario);
        Optional<Usuario> found = usuarioRepository.findByEmail(usuario.getEmail());
        
        assertTrue(found.isPresent());
        assertEquals(saved.getEmail(), found.get().getEmail());
    }
}
```

### Prueba de Controller con MockMvc

```java
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testLogin_Success() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }
}
```

## 📝 Comandos Útiles

```bash
# Ejecutar solo pruebas rápidas (excluir integración)
./mvnw test -Dgroups="unit"

# Ejecutar en modo debug
./mvnw test -Dmaven.surefire.debug

# Saltar pruebas (NO RECOMENDADO en desarrollo)
./mvnw install -DskipTests

# Ver solo pruebas fallidas
./mvnw test | grep -A 10 "FAILURE"
```

## 🐛 Solución de Problemas

### Problema: Pruebas fallan por configuración de base de datos

**Solución:** Las pruebas usan H2 en memoria por defecto. Verifica `application.properties` en `src/test/resources/`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

### Problema: MockBean deprecated warning

**Solución:** Es un warning conocido en Spring Boot 3.4+. Las pruebas funcionan correctamente.

### Problema: JwtService tests fallan

**Solución:** Verifica que el `secretKey` en el test sea válido en Base64.

## 📚 Recursos Adicionales

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [AssertJ Documentation](https://assertj.github.io/doc/)

## ✅ Checklist de Calidad

Antes de hacer commit, verifica:

- [ ] Todas las pruebas pasan (`./mvnw test`)
- [ ] No hay pruebas ignoradas (`@Disabled`)
- [ ] Cobertura de código > 80%
- [ ] Nombres descriptivos en `@DisplayName`
- [ ] Cada prueba verifica UNA cosa
- [ ] No hay código duplicado en pruebas
- [ ] Mock objects configurados correctamente

## 🎓 Convenciones de Nomenclatura

- **Clases:** `ClaseQueSeProbarTest`
- **Métodos:** `test[NombreMetodo]_[Escenario]`
- **Ejemplos:**
  - `testGuardarUsuario_Success`
  - `testLogin_InvalidCredentials`
  - `testBuscarPorId_NotFound`

---

**¡Las pruebas son código de producción! Mantenlas limpias y legibles.** 🚀
