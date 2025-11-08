class Task {
  final int? id;
  final String? title;
  final String? description;
  final String? status;
  final String? category;
  final String? priority;
  final String? createdAt;
  final List<String>? subtasks;
  final List<String>? attachments;
  final bool? completed;

  Task({
    this.id,
    this.title,
    this.description,
    this.status,
    this.category,
    this.priority,
    this.createdAt,
    this.subtasks,
    this.attachments,
    this.completed,
  });

  factory Task.fromJson(Map<String, dynamic> json) {
    // Helper function to safely convert to List<String>
    List<String>? _parseStringList(dynamic value) {
      if (value == null) return null;
      if (value is List) {
        return value.map((item) {
          if (item is String) {
            return item;
          } else if (item is Map) {
            // If it's a map, try to extract a string value
            return item['name']?.toString() ?? 
                   item['title']?.toString() ?? 
                   item['text']?.toString() ?? 
                   item.toString();
          } else {
            return item.toString();
          }
        }).toList();
      } else if (value is String) {
        return [value];
      }
      return null;
    }

    return Task(
      id: json['id'] is int ? json['id'] : (json['id'] is String ? int.tryParse(json['id']) : null),
      title: json['title']?.toString(),
      description: json['description']?.toString(),
      status: json['status']?.toString(),
      category: json['category']?.toString(),
      priority: json['priority']?.toString(),
      createdAt: json['createdAt']?.toString() ?? json['created_at']?.toString(),
      subtasks: _parseStringList(json['subtasks']),
      attachments: _parseStringList(json['attachments']),
      completed: json['completed'] is bool ? json['completed'] : (json['completed'] == true || json['completed'] == 1 || json['completed'] == 'true'),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'description': description,
      'status': status,
      'category': category,
      'priority': priority,
      'createdAt': createdAt,
      'subtasks': subtasks,
      'attachments': attachments,
      'completed': completed,
    };
  }
}

