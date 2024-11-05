package es.alten.utils;

import es.alten.domain.ElvisEntity;
import es.alten.dto.ElvisBaseDTO;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class ListMapper implements Serializable {

  private static final long serialVersionUID = 3023814570312140936L;

  /**
   * Converts a list of T into a list of D.
   *
   * @param inputEntity the list to convert
   * @param outputDTO the class of the output
   * @param <T> type of the input
   * @param <D> type of the output
   * @return a list of D entities
   * @throws IllegalAccessException if inputEntity has no elements
   * @throws InstantiationException if D has no default constructor
   * @throws NoSuchMethodException if getDeclaredConstructor error
   * @throws InvocationTargetException if newInstance error
   * @noinspection unchecked
   */
  public static <T extends ElvisEntity, D extends ElvisBaseDTO> List<D> map(
      final List<T> inputEntity, final Class<D> outputDTO)
      throws IllegalAccessException, InstantiationException, NoSuchMethodException,
          InvocationTargetException {
    final List<D> result = new LinkedList<>();
    for (final T i : inputEntity) {
      final D o = outputDTO.getDeclaredConstructor().newInstance();
      o.loadFromDomain(i);
      result.add(o);
    }
    return result;
  }

  /**
   * Converts a list of T into a list of D.
   *
   * @noinspection unchecked, unused
   */
  public static <T extends ElvisEntity, D extends ElvisBaseDTO> List<T> map(
      final List<D> inputDTO) {
    final List<T> result = new LinkedList<>();

    for (final D i : inputDTO) {
      final T o = (T) i.obtainDomainObject();
      result.add(o);
    }
    return result;
  }
}
