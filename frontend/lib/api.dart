import 'dart:convert';
import 'dart:async';
import 'package:http/http.dart' as http;

class Session { final String token; final String nombre; final String rol; Session({required this.token, required this.nombre, required this.rol}); }

class Api {
  static const baseUrl = String.fromEnvironment('API_BASE_URL', defaultValue: 'http://127.0.0.1:8080/api');
  static Future<Session> login(String email, String password) async {
    final response = await http.post(Uri.parse('$baseUrl/auth/login'), headers: {'Content-Type': 'application/json'}, body: jsonEncode({'email': email, 'password': password})).timeout(const Duration(seconds: 8), onTimeout: () => throw TimeoutException('No se pudo conectar con el servidor'));
    if (response.statusCode != 200) throw Exception('Correo o contraseña incorrectos');
    final data = jsonDecode(response.body); return Session(token: data['token'], nombre: data['nombre'], rol: data['rol']);
  }
  static Future<void> register(String nombre, String email, String telefono, String password) async {
    final response = await http.post(Uri.parse('$baseUrl/auth/register'), headers: _headers(''), body: jsonEncode({'nombre': nombre, 'email': email, 'telefono': telefono, 'password': password})).timeout(const Duration(seconds: 8), onTimeout: () => throw TimeoutException('No se pudo conectar con el servidor'));
    if (response.statusCode != 201) throw Exception('No se pudo crear la cuenta');
  }
  static Future<List<dynamic>> categories(String token) async => _list('/categorias', token);
  static Future<List<dynamic>> users(String token) async => _list('/usuarios', token);
  static Future<void> createCategory(String token, String nombre, String area) async { final response = await http.post(Uri.parse('$baseUrl/categorias'), headers: _headers(token), body: jsonEncode({'nombre': nombre, 'area': area})); if (response.statusCode >= 300) throw Exception('No se pudo crear la categoría'); }
  static Future<void> updateCategory(String token, int id, String nombre, String area) async { final response = await http.put(Uri.parse('$baseUrl/categorias/$id'), headers: _headers(token), body: jsonEncode({'nombre': nombre, 'area': area})); if (response.statusCode >= 300) throw Exception('No se pudo actualizar la categoría'); }
  static Future<void> deleteCategory(String token, int id) async { final response = await http.delete(Uri.parse('$baseUrl/categorias/$id'), headers: _headers(token)); if (response.statusCode >= 300) throw Exception('No se pudo desactivar la categoría'); }
  static Future<void> updateUser(String token, int id, String nombre, String rol, String telefono, bool activo) async { final response = await http.put(Uri.parse('$baseUrl/usuarios/$id'), headers: _headers(token), body: jsonEncode({'nombre': nombre, 'rol': rol, 'telefono': telefono, 'activo': activo})); if (response.statusCode >= 300) throw Exception('No se pudo actualizar el usuario'); }
  static Future<void> deleteUser(String token, int id) async { final response = await http.delete(Uri.parse('$baseUrl/usuarios/$id'), headers: _headers(token)); if (response.statusCode >= 300) throw Exception('No se pudo desactivar el usuario'); }
  static Future<List<dynamic>> _list(String path, String token) async { final response = await http.get(Uri.parse('$baseUrl$path'), headers: _headers(token)); if (response.statusCode >= 300) throw Exception('No se pudieron cargar los datos'); return jsonDecode(response.body); }
  static Map<String, String> _headers(String token) => {'Content-Type': 'application/json', 'Authorization': 'Bearer $token'};
}