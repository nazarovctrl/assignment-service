package uz.ccrew.assignmentservice.base;

import java.util.List;

public interface Mapper<C, D, E> {
    E toEntity(C c);

    D toDTO(E e);

    default List<D> toDTOList(List<E> eList) {
        return eList.stream().map(this::toDTO).toList();
    }
}