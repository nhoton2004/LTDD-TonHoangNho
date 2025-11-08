import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/task.dart';

class ApiService {
  static const String baseUrl = 'https://amock.io/api/researchUTH';

  // Lấy tất cả các tasks
  static Future<List<Task>> getAllTasks() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/tasks'));
      
      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        if (data is List) {
          return data.map((item) => Task.fromJson(item)).toList();
        } else if (data is Map && data.containsKey('data') && data['data'] is List) {
          return (data['data'] as List)
              .map((item) => Task.fromJson(item))
              .toList();
        }
        return [];
      } else {
        throw Exception('Failed to load tasks: ${response.statusCode}');
      }
    } catch (e) {
      print('Error fetching tasks: $e');
      throw e;
    }
  }

  // Lấy chi tiết một task theo ID
  static Future<Task> getTaskDetail(int taskId) async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/task/$taskId'));
      
      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return Task.fromJson(data);
      } else {
        throw Exception('Failed to load task detail: ${response.statusCode}');
      }
    } catch (e) {
      print('Error fetching task detail: $e');
      throw e;
    }
  }

  // Xóa một task theo ID
  static Future<void> deleteTask(int taskId) async {
    try {
      final response = await http.delete(Uri.parse('$baseUrl/task/$taskId'));
      
      if (response.statusCode != 200 && response.statusCode != 204) {
        throw Exception('Failed to delete task: ${response.statusCode}');
      }
    } catch (e) {
      print('Error deleting task: $e');
      throw e;
    }
  }
}

